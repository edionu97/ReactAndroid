package services.implementations;

import models.Contact;
import models.User;
import models.UserContact;
import persistence.interfaces.ContactRepository;
import persistence.interfaces.UserContactRepository;
import persistence.interfaces.UserRepository;
import services.interfaces.UserContactService;
import java.util.Optional;

public class UserContactServiceImpl implements UserContactService {

    public UserContactServiceImpl(UserRepository userRepository, UserContactRepository userContactRepository, ContactRepository contactRepository){
        this.userRepository = userRepository;
        this.userContractRepository = userContactRepository;
        this.contactRepository  = contactRepository;
    }

    @Override
    public void addContact(String username, String firstName, String lastName, String phoneNumber) throws Exception {

        User user = userRepository.findByUsername(username);

        if(user == null){
            throw new Exception("User does not exist!");
        }

        Contact contact = new Contact(firstName, lastName, phoneNumber);

        if(user.getUserContactList().stream().anyMatch(x->x.getContact().getFirstName().equals(contact.getFirstName()))){
            throw new Exception("Duplicates not allowed!");
        }

        userContractRepository.add(user, contact);
    }

    @Override
    public void updateContact(String username, String firstName, String lastName, String phoneNumber) throws Exception {

        Optional<Contact> contact = userRepository
                .findByUsername(username)
                .getUserContactList()
                .stream()
                .filter(
                        x->x.getContact().getFirstName().equals(firstName)
                )
                .map(UserContact::getContact)
                .findFirst();


        if(!contact.isPresent()){
            throw new Exception("Contact does not exist!");
        }

        contact.get().setLastName(lastName);
        contact.get().setPhoneNumber(phoneNumber);

        contactRepository.update(contact.get());
    }

    @Override
    public void deleteContact(String username, String firstName) throws Exception {

        Optional<Contact> contact = userRepository
                .findByUsername(username)
                .getUserContactList()
                .stream()
                .filter(
                        x->x.getContact().getFirstName().equals(firstName)
                )
                .map(UserContact::getContact)
                .findFirst();

        if(!contact.isPresent()){
            throw new Exception("Contact does not exist!");
        }

        contactRepository.delete(contact.get());
    }


    private UserContactRepository userContractRepository;
    private UserRepository userRepository;
    private ContactRepository contactRepository;
}
