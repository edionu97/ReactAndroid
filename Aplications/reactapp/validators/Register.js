export default class RegisterValidator {
  static validate(username, password, confirm) {
    if (password === null || password === '') {
      throw new Error('Password should not be empty');
    }

    if (username === null || username === '') {
      throw new Error('Username should not be empty');
    }

    if (confirm === null || confirm === ' ') {
      throw new Error('Confirmation should not be empty');
    }

    if (confirm !== password) {
      throw new Error('Passwords are not equal');
    }
  }
}
