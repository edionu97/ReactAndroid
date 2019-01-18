package security.providers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class TokenProvider {

    public Claims parseJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(TokenExtraInfo.SECRET))
                .parseClaimsJws(jwt).getBody();
    }

    public String getSubject(String jwt){
        return parseJWT(removeBeforeHeader(jwt)).getSubject();
    }

    public String createJWT(String username, boolean willExpire) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(TokenExtraInfo.SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setSubject(username)
                .signWith(signatureAlgorithm, signingKey);

        if (willExpire) {
            long expMillis = nowMillis + TokenExtraInfo.EXPIRATION_TIME;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        return builder.compact();
    }

    private String removeBeforeHeader(String jwt){
        return jwt.substring(TokenExtraInfo.BEFORE_HEADER.length());
    }

}
