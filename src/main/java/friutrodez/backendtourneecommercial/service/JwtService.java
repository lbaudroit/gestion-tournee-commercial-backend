package friutrodez.backendtourneecommercial.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service pour gérer les tokens
 */
@Service
public class JwtService  {

    /**
     * clé d'encryption encrypté en HMAC-SHA256
     * On peut utiliser ce site <a href="https://www.devglan.com/online-tools/hmac-sha256-online?ref=blog.tericcabrel.com">...</a>
     * pour créer la clé
     **/
    private final String CLE_ENCRYPTION = "gd5kn1HM/aj36IU7VsRRwrPuYiFvOYQdB8yIdA4UIaU=";

    /** durée en minutes du token **/
    private final int MINUTES = 30;

    /** durée totale du token **/
    public final int JWT_EXPIRATION = MINUTES * 60 * 1000;
    public String extraireNomUtilisateur(String token) {
        return extraireClaim(token, Claims::getSubject);
    }

    private <T> T extraireClaim(String token, Function<Claims,T> sujet) {
        final Claims claims = extraireTousClaims(token);
        return sujet.apply(claims);
    }
    public String genererToken(UserDetails userDetails) {
        return genererToken(new HashMap<String,Object>(), userDetails);
    }

    public String genererToken(HashMap<String, Object> claimsExtras, UserDetails userDetails) {
        return construireToken(claimsExtras, userDetails, JWT_EXPIRATION);
    }


    /**
     *
     * @param claimsExtras les claims à mettre dans le token
     * @param userDetails l'username de l'utilisateur
     * @param expiration la date d'expiration
     * @return un token signé avec son payload
     */
    private String construireToken(
            HashMap<String, Object> claimsExtras,
            UserDetails userDetails,
            long expiration
    ) {
        String sel = genererSel();


        return Jwts
                .builder()
                .setClaims(claimsExtras)

                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .setId(sel)

                .signWith(obtenirCleSignature(CLE_ENCRYPTION), SignatureAlgorithm.HS256)
                .compact();
    }
    public boolean tokenEstValide(String token, UserDetails userDetails) {
        final String username = extraireNomUtilisateur(token);
        return (username.equals(userDetails.getUsername())) && !tokenEstExpire(token);
    }

    public boolean tokenEstExpire(String token) {
        return extraireExpiration(token).before(new Date());
    }

    /**
     *
     * @param token
     * @return la date d'expiration
     */
    public Date extraireExpiration(String token) {
        return extraireClaim(token, Claims::getExpiration);
    }

    /**
     * Extrait les claims. Vérifie avant si la signature du token est correcte avec la clé d'encryption
     * @param token
     * @return les claims du token
     */
    public Claims extraireTousClaims(String token) {
        return Jwts.parserBuilder().
                setSigningKey(obtenirCleSignature(CLE_ENCRYPTION)).
                build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Génération de sel pour varier les tokens par utilisateur
     * @return une string encodé en base64
     */
    private String genererSel() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * @param cleEncryption la clé utilisée pour cryptée
     * @return une clé hashé en hmac256
     */
    private SecretKey obtenirCleSignature(String cleEncryption) {
        byte[] keyBytes = Decoders.BASE64.decode(cleEncryption);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
