package controllers.messages;

import models.Contact;

import java.io.Serializable;
import java.util.List;

public class AllContactsMessage implements Serializable {

    public AllContactsMessage() {
    }

    public AllContactsMessage(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<Contact> contacts;
}
