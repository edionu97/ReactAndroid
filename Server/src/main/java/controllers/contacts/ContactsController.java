package controllers.contacts;

import controllers.messages.AddContactMessage;
import controllers.messages.AllContactsMessage;
import controllers.messages.DeleteContactMessage;
import controllers.messages.UpdateContactMessage;
import models.Contact;
import models.User;
import models.UserContact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import security.providers.TokenExtraInfo;
import security.providers.TokenProvider;
import services.interfaces.UserContactService;
import services.interfaces.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/contacts")
@ComponentScan(basePackages = "configuration")
public class ContactsController {

    @Autowired
    public ContactsController(UserContactService contactService, UserService userService, TokenProvider tokenProvider){
        this.contactService = contactService;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addContact(
            @RequestHeader("Authentication") String authenticationHeader,
            @RequestBody AddContactMessage contactMessage){

        if (authenticationHeader == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (authenticationHeader.length() <= TokenExtraInfo.BEFORE_HEADER.length() || contactMessage == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String username = tokenProvider.getSubject(authenticationHeader);

        try{
            contactService.addContact(
                    username,
                    contactMessage.getFirstName(),
                    contactMessage.getLastName(),
                    contactMessage.getPhoneNumber()
            );
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateContact(
            @RequestHeader("Authentication") String authenticationHeader,
            @RequestBody UpdateContactMessage contactMessage){

        if (authenticationHeader == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (authenticationHeader.length() <= TokenExtraInfo.BEFORE_HEADER.length() || contactMessage == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String username = tokenProvider.getSubject(authenticationHeader);

        try{
            contactService.updateContact(
                    username,
                    contactMessage.getFirstName(),
                    contactMessage.getLastName(),
                    contactMessage.getPhoneNumber()
            );
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteContact(
            @RequestHeader("Authentication") String authenticationHeader,
            @RequestBody DeleteContactMessage contactMessage){

        if (authenticationHeader == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (authenticationHeader.length() <= TokenExtraInfo.BEFORE_HEADER.length() || contactMessage == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String username = tokenProvider.getSubject(authenticationHeader);

        try{
            contactService.deleteContact(
                    username,
                    contactMessage.getFirstName()
            );
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<AllContactsMessage> allContacts(@RequestHeader("Authentication") String authenticationHeader){

        if (authenticationHeader == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (authenticationHeader.length() <= TokenExtraInfo.BEFORE_HEADER.length()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String username = tokenProvider.getSubject(authenticationHeader);

        User user  = userService.findUserByUsername(username);

        List<Contact> contacts = user
                .getUserContactList()
                .stream()
                .map(UserContact::getContact)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new AllContactsMessage(contacts), HttpStatus.OK);
    }



    private UserContactService contactService;
    private UserService userService;
    private TokenProvider tokenProvider;
}
