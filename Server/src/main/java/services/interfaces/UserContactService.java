package services.interfaces;


import models.Contact;

public interface UserContactService {

    void addContact(
            String username,
            String firstName,
            String lastName,
            String phoneNumber) throws Exception;

    void updateContact(String username,
                       String firstName,
                       String lastName,
                       String phoneNumber) throws  Exception;

    void deleteContact(String username, String firstName) throws  Exception;

}
