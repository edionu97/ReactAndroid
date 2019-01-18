export default class ContactValidator {
  static validate(username, lastName, phoneNumber) {
    if (username === null || username === '') {
      throw new Error('First Name should not be empty');
    }

    if (lastName === null || lastName === '') {
      throw new Error('Last Name should not be empty');
    }

    if (phoneNumber === null || phoneNumber === ' ') {
      throw new Error('Phone number should not be empty');
    }
  }
}
