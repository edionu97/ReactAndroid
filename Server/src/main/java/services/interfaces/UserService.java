package services.interfaces;


import models.User;

public interface UserService{
    /**
     * Checks if in database exist a user with the password equal to password and
     * username equal with username
     * @param username: string, represents the entered username
     * @param password: string, represents the entered password
     * @return true if the user can authenticate or false contrary
     */
    boolean login(String username, String password);

    void signUp(String username, String encodedPassword) throws Exception;

    User findUserByUsername(String username);
}
