package friutrodez.backendtourneecommercial.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService  {

    /** clé d'encryption encrypté en HMAC-SHA256
     * On peut utiliser ce site <a href="https://www.devglan.com/online-tools/hmac-sha256-online?ref=blog.tericcabrel.com">...</a>
     * pour créer la clé
     **/
    private final String CLE_ENCRYPTION = "gd5kn1HM/aj36IU7VsRRwrPuYiFvOYQdB8yIdA4UIaU=";

    /** durée en minutes du token **/
    private final int MINUTES = 30;

    /** durée totale du token **/
    public final int JWT_EXPIRATION = MINUTES * 60 * 1000;
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims,T> subject) {
        final Claims claims = extractAllClaims(token);
        return subject.apply(claims);
    }
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<String,Object>(), userDetails);
    }

    public String generateToken(HashMap<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, JWT_EXPIRATION);
    }
    

    private String buildToken(
            HashMap<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        String sel = genererSel();

        extraClaims.put("username",userDetails.getUsername());

        return Jwts
                .builder()
                .setClaims(extraClaims)

                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .setId(sel)

                .signWith(getSigningKey(CLE_ENCRYPTION), SignatureAlgorithm.HS256)
                .compact();
    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().
                setSigningKey(getSigningKey(CLE_ENCRYPTION)).
                build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String genererSel() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    private SecretKey getSigningKey(String cleEncryption) {
        byte[] keyBytes = Decoders.BASE64.decode(cleEncryption);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
