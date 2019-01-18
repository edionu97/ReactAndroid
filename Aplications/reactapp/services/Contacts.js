import OnlineContactService from './Online';
import OfflineContactService from './Offline';

export default class ContactService {
  static instance = null;
  static online = null;
  static offline = null;

  static getInstance() {
    return ContactService.instance;
  }

  static setOnlineInstance() {
    if (ContactService.online === null) {
      ContactService.online = new OnlineContactService();
    }

    ContactService.instance = ContactService.online;
  }

  static setOfflineInstance() {
    if (ContactService.offline === null) {
      ContactService.offline = new OfflineContactService();
    }
    ContactService.instance = ContactService.offline;
  }

  static getOnlineInstance() {
    if (ContactService.online === null) {
      ContactService.online = new OnlineContactService();
    }
    return ContactService.online;
  }
}
