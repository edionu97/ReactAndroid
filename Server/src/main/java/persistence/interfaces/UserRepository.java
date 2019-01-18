package persistence.interfaces;

import models.User;
import java.util.List;

public interface UserRepository {

    boolean login(String username, String password);

    User findByUsername(String username);

    void createAccount(String username, String password) throws Exception;

    List<User> getAllUsers();
}

