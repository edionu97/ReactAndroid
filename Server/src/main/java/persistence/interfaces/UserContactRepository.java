package persistence.interfaces;

import models.Contact;
import models.User;
import models.UserContact;
import persistence.utils.SimpleCRUD;

import java.util.List;

public interface UserContactRepository extends SimpleCRUD<UserContact> {

    void add(User user, Contact contact) throws  Exception;

    UserContact findContactById(int uid, int cid);

    List<UserContact> getAll();
}
