package controllers.login;

import controllers.messages.AuthenticationMessage;
import controllers.messages.LogoutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import security.WebSecurityJWT;
import security.providers.TokenExtraInfo;
import services.interfaces.UserService;
import sun.misc.BASE64Encoder;


@RestController
@RequestMapping("/user")
@ComponentScan(basePackages = "configuration")
public class UserController {

    @Autowired
    public UserController(UserService service,
                          BASE64Encoder encoder,
                          WebSecurityJWT securityJWT){
        this.service = service;
        this.encoder = encoder;
        this.securityJWT = securityJWT;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationMessage message){


        if(message.getPassword() == null || message.getUsername() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String password = encoder.encode(message.getPassword().getBytes());

        if(!service.login(message.getUsername(), password)){
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(securityJWT.createAuthenticationHeaders(message.getUsername()), HttpStatus.ACCEPTED);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authentication") String authenticationHeader,
                                    @RequestBody LogoutMessage message){

        if(message.getUsername() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(authenticationHeader.length() <= TokenExtraInfo.BEFORE_HEADER.length()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(securityJWT.isAuthenticated(message.getUsername(),
                authenticationHeader.substring(TokenExtraInfo.BEFORE_HEADER.length())
        )){
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody AuthenticationMessage message){

        if(message.getUsername() == null || message.getPassword() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        ResponseEntity<String> responseEntity;

        try {
            service.signUp(
                    message.getUsername(),
                    encoder.encode(message.getPassword().getBytes())
            );

            responseEntity = new ResponseEntity<>(
                    securityJWT.createAuthenticationHeaders(message.getUsername()),
                    HttpStatus.ACCEPTED);

        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.FOUND);
        }

        return responseEntity;
    }

    private UserService service;
    private BASE64Encoder encoder;
    private WebSecurityJWT securityJWT;
}
