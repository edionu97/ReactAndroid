package services.implementations;

import models.User;
import org.springframework.stereotype.Repository;
import persistence.interfaces.UserRepository;
import services.interfaces.UserService;

@Repository
public class UserServiceImpl implements UserService {

    public UserServiceImpl(UserRepository repository){
        this.repository = repository;
    }

    @Override
    public boolean login(String username, String password) {
        return repository.login(username, password);
    }

    @Override
    public void signUp(String username, String encodedPassword) throws  Exception{
        repository.createAccount(username, encodedPassword);
    }

    @Override
    public User findUserByUsername(String username) {
        return repository.findByUsername(username);
    }

    private UserRepository repository;
}
