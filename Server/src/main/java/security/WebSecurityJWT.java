package security;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import security.providers.TokenExtraInfo;
import security.providers.TokenProvider;

public class WebSecurityJWT {

    public WebSecurityJWT(TokenProvider provider){
        this.tokenProvider = provider;
    }

    public HttpHeaders createAuthenticationHeaders(String username){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(
                TokenExtraInfo.HEADER_NAME,
                TokenExtraInfo.BEFORE_HEADER + tokenProvider.createJWT(username, false)
        );
        return  httpHeaders;
    }

    public boolean isAuthenticated(String username, String jwt){
        try {
            Claims claims = tokenProvider.parseJWT(jwt);
            return username.equals(claims.getSubject());
        }catch (Exception e){
            return false;
        }
    }

    private  TokenProvider tokenProvider;
}
