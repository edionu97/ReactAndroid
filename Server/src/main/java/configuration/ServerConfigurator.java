package configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import persistence.implementations.ContactRepositoryImpl;
import persistence.implementations.FetchDetailsRepositoryImpl;
import persistence.implementations.UserContactRepositoryImpl;
import persistence.implementations.UserRepositoryImpl;
import persistence.interfaces.ContactRepository;
import persistence.interfaces.FetchDetailsRepository;
import persistence.interfaces.UserContactRepository;
import persistence.interfaces.UserRepository;
import security.WebSecurityJWT;
import security.providers.TokenProvider;
import services.implementations.FetchServiceImpl;
import services.implementations.UserContactServiceImpl;
import services.implementations.UserServiceImpl;
import services.interfaces.FetchService;
import services.interfaces.UserContactService;
import services.interfaces.UserService;
import sun.misc.BASE64Encoder;

@Configuration
public class ServerConfigurator {

    @Bean
    public UserService service() {
        return new UserServiceImpl(userRepository());
    }

    @Bean
    public UserContactService userContactService() {
        return new UserContactServiceImpl(userRepository(), userContactRepository(), contactRepository());
    }

    @Bean
    public FetchService fetchService() {
        return new FetchServiceImpl(fetchDetailsRepository());
    }

    @Bean
    public BASE64Encoder encoder() {
        return new BASE64Encoder();
    }

    @Bean
    public TokenProvider tokenProvider() {
        return new TokenProvider();
    }

    @Bean
    public WebSecurityJWT webSecurityJWT() {
        return new WebSecurityJWT(tokenProvider());
    }

    @Bean
    public ContactRepository contactRepository() {
        return new ContactRepositoryImpl();
    }

    @Bean
    public UserContactRepository userContactRepository() {
        return new UserContactRepositoryImpl();
    }

    @Bean
    public FetchDetailsRepository fetchDetailsRepository() {
        return new FetchDetailsRepositoryImpl();
    }

    @Bean
    public UserRepository userRepository() {
        return new UserRepositoryImpl();
    }
}
