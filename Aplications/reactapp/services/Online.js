import url from './utils/url';

export default class OnlineContactService {
  timeout(ms, promise) {
    return new Promise(function(resolve, reject) {
      setTimeout(function() {
        reject(new Error('Could not connect to server'));
      }, ms);
      promise.then(resolve, reject);
    });
  }

  /**
   *
   * @param {*Representing the given token} token
   */
  getAllContacts(token) {
    return this.timeout(
      5000,
      fetch(url.server + url.allContactUrl, {
        method: 'GET',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
          Authentication: token
        },
        timeout: 5000
      }).then(resp => {
        if (!resp.ok) {
          return resp;
        }
        return resp.json();
      })
    );
  }

  /**
   *
   * @param {*Represents the token} token
   * @param {*Data to be inserted} contact
   */
  addContact(token, contact) {
    return this.timeout(
      5000,
      fetch(url.server + url.addContactUrl, {
        method: 'POST',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
          Authentication: token
        },
        timeout: 5000,
        body: JSON.stringify(contact)
      })
    );
  }

  removeContact(token, contact_id) {
    return this.timeout(
      5000,
      fetch(url.server + url.deleteContactUrl, {
        method: 'POST',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
          Authentication: token
        },
        timeout: 5000,
        body: JSON.stringify({
          first: contact_id
        })
      })
    );
  }

  updateContact(token, contact) {
    return this.timeout(
      5000,
      fetch(url.server + url.updateContactUrl, {
        method: 'POST',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
          Authentication: token
        },
        timeout: 5000,
        body: JSON.stringify(contact)
      })
    );
  }
}
