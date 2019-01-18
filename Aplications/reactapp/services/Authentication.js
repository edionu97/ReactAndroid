import url from './utils/url';

export default class AuthenticationService {
  static instance = null;

  constructor() {}

  static getInstance() {
    if (AuthenticationService.instance === null) {
      AuthenticationService.instance = new AuthenticationService();
    }

    return AuthenticationService.instance;
  }

  /**
   * Set's a timeout in case that the connection to the server is not available
   * @param {String} ms
   * @param {Promise} promise
   */
  timeout(ms, promise) {
    return new Promise(function(resolve, reject) {
      setTimeout(function() {
        reject(new Error('Could not connect to server'));
      }, ms);
      promise.then(resolve, reject);
    });
  }

  /**
   * Creates a new accound, if possible
   * @param {*} username: String, representing the username
   * @param {*} password: String, representing the password
   */
  register(username, password) {
    return this.timeout(
      5000,
      fetch(url.server + url.registerUrl, {
        method: 'POST',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json'
        },
        timeout: 5000,
        body: JSON.stringify({
          user: username,
          pass: password
        })
      }).then(res => {
        res.message = 'Already exists an user with that username!';

        if (res.status === 202) {
          res.message = 'Account created successfully!';
          return res;
        }

        throw res;
      })
    );
  }

  /**
   * Called when user atempts to login
   * @param {String representing the username} username
   * @param {String representing the passwod} password
   */
  login(username, password) {
    return this.timeout(
      5000,
      fetch(url.server + url.login, {
        method: 'POST',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json'
        },
        timeout: 5000,
        body: JSON.stringify({
          user: username,
          pass: password
        })
      })
    );
  }

  /**
   * use for logout
   */
  logout(token, username) {
    return this.timeout(
      5000,
      fetch(url.server + url.logout, {
        method: 'POST',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
          Authentication: token
        },
        timeout: 5000,
        body: JSON.stringify({
          user: username
        })
      })
    );
  }
}
