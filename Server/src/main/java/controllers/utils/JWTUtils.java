package controllers.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import security.WebSecurityJWT;
import security.providers.TokenExtraInfo;

public class JWTUtils {

    public  static  boolean isOk(String authenticationHeader, String username, WebSecurityJWT securityJWT){

        if(authenticationHeader.length() <= TokenExtraInfo.BEFORE_HEADER.length()){
            return false;
        }

        return  securityJWT.isAuthenticated(
                    username,
                    authenticationHeader.substring(TokenExtraInfo.BEFORE_HEADER.length())
        );
    }
}
