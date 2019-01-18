// userData :{
// 	contacts: [],
// 	unfetched:[]
// }

import { AsyncStorage } from 'react-native';
import ContactService from './Contacts';
import OnlineContactService from './Online';

export default class OfflineContactService {
  constructor() {}

  async getAllContacts(token) {
    const username = await AsyncStorage.getItem('username');
    return AsyncStorage.getItem(username).then(resp => {
      if (resp === null) {
        resp = JSON.stringify({
          contacts: []
        });
      }
      return JSON.parse(resp);
    });
  }

  async addContact(token, contact) {
    const username = await AsyncStorage.getItem('username');
    const userData = JSON.parse(await AsyncStorage.getItem(username));

    let contacts = userData.contacts;

    if (contacts.find(ct => ct.firstName === contact.first)) {
      return {
        ok: false
      };
    }

    contacts.push({
      firstName: contact.first,
      lastName: contact.last,
      phoneNumber: contact.phone
    });

    userData.unfetched.push({
      data: {
        firstName: contact.first,
        lastName: contact.last,
        phoneNumber: contact.phone
      },
      user: username,
      type: 'insert'
    });

    return AsyncStorage.setItem(username, JSON.stringify(userData)).then(
      resp => {
        resp = {
          ok: true
        };
        return resp;
      }
    );
  }

  async removeContact(token, contact_id) {
    const username = await AsyncStorage.getItem('username');
    const userData = JSON.parse(await AsyncStorage.getItem(username));

    userData.contacts = userData.contacts.filter(contact => {
      return contact.firstName !== contact_id;
    });

    userData.unfetched.push({
      data: {
        firstName: contact_id
      },
      user: username,
      type: 'delete'
    });

    return AsyncStorage.setItem(username, JSON.stringify(userData)).then(
      resp => {
        resp = {
          ok: true
        };
        return resp;
      }
    );
  }

  async updateContact(token, contact) {
    const username = await AsyncStorage.getItem('username');
    const userData = JSON.parse(await AsyncStorage.getItem(username));

    let contacts = userData.contacts;

    let ctct = contacts.find(ct => ct.firstName === contact.first);

    if (!ctct) {
      return {
        ok: false
      };
    }

    ctct.firstName = contact.first;
    ctct.lastName = contact.last;
    ctct.phoneNumber = contact.phone;

    userData.unfetched.push({
      data: {
        firstName: contact.first,
        lastName: contact.last,
        phoneNumber: contact.phone
      },
      user: username,
      type: 'update'
    });

    return AsyncStorage.setItem(username, JSON.stringify(userData)).then(
      resp => {
        resp = {
          ok: true
        };
        return resp;
      }
    );
  }
}
