package persistence.interfaces;

import models.Contact;
import persistence.utils.SimpleCRUD;

import java.util.List;

public interface ContactRepository extends SimpleCRUD<Contact> {

    void add(String firstName, String lastName, String phoneNumber) throws  Exception;

    Contact findContactByFirstName(String firstName);

    Contact findContactById(int cid);

    List<Contact> getAll();
}
