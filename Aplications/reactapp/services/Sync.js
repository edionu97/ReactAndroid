import { AsyncStorage, NetInfo } from 'react-native';
import ContactService from './Contacts';

export default class SyncService {
  constructor() {}

  startSync(afterSync) {
    this.syncInterval = setInterval(() => this.syncFunction(afterSync), 5000);
  }

  stopSync() {
    clearInterval(this.syncInterval);
  }

  async syncFunction(afterSync) {
    const username = await AsyncStorage.getItem('username');
    const jwt = await AsyncStorage.getItem('jwt');

    if (username === null || jwt === null) {
      return;
    }

    let userData = await AsyncStorage.getItem(username);

    if (!(await NetInfo.isConnected.fetch())) {
      return;
    }

    if (userData === null) {
      this.getDataFromServer(jwt, username, afterSync);
      return;
    }

    userData = JSON.parse(userData);

    console.log(userData.unfetched);

    if (userData.unfetched.length !== 0) {
      this.propagateChangesOnServer(
        userData,
        userData.unfetched,
        jwt,
        username,
        afterSync
      );
      return;
    }

    // get data from server
    ContactService.getOnlineInstance()
      .getAllContacts(jwt)
      .then(resp => {
        userData.contacts = resp.contacts;
        AsyncStorage.setItem(username, JSON.stringify(userData)).then(resp => {
          afterSync();
        });
      })
      .catch(ex => {});
  }

  getDataFromServer(jwt, username, callback) {
    let userData = {
      contacts: [],
      unfetched: []
    };

    ContactService.getOnlineInstance()
      .getAllContacts(jwt)
      .then(resp => {
        userData.contacts = resp.contacts;
        AsyncStorage.setItem(username, JSON.stringify(userData)).then(resp => {
          callback();
        });
      })
      .catch(ex => {
        console.log(ex);
      });
  }

  async propagateChangesOnServer(userData, unfeched, jwt, username, callback) {
    let service = ContactService.getOnlineInstance();
    let fetched = [];
    for (var index in unfeched) {
      var fetch = unfeched[index];
      switch (fetch.type) {
        case 'insert': {
          try {
            var resp = await service.addContact(jwt, {
              first: fetch.data.firstName,
              last: fetch.data.lastName,
              phone: fetch.data.phoneNumber
            });
            callback();
          } catch (ex) {
            fetched.push(fetch);
          }
          break;
        }
        case 'delete': {
          try {
            var resp = await service.removeContact(jwt, fetch.data.firstName);
            callback();
          } catch (ex) {
            fetched.push(fetch);
          }
          break;
        }
        case 'update': {
          try {
            var resp = await service.updateContact(jwt, {
              first: fetch.data.firstName,
              last: fetch.data.lastName,
              phone: fetch.data.phoneNumber
            });
            callback();
          } catch (ex) {
            fetched.push(fetch);
          }
          break;
        }
      }
    }
    userData.unfetched = fetched;
    await AsyncStorage.setItem(username, JSON.stringify(userData));
  }

  static getInstance() {
    if (SyncService.instance === null) {
      SyncService.instance = new SyncService();
    }
    return SyncService.instance;
  }

  static instance = null;
}
