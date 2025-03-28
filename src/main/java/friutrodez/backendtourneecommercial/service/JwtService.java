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
import java.util.function.Function;

/**
 * Service pour gérer les tokens.
 * Un JWT Token est composé de trois parties.
 * Le header : le type d'algo encryption et le type du token
 * Le payload : contient les informations du token
 * La signature : la clé du token (utilisée pour valider le token).
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
@Service
public class JwtService {

    public static final int EXPIRATION_TOKEN = 1800000;
    /**
     * Clé d'encryption encrypté en HMAC-SHA256.
     * On peut utiliser ce site…<a href="https://www.devglan.com/online-tools/hmac-sha256-online?ref=blog.tericcabrel.com"></a>
     * Pour créer la clé
     **/
    private static final String ENCRYPTION_KEY = "gd5kn1HM/aj36IU7VsRRwrPuYiFvOYQdB8yIdA4UIaU=";

    /**
     * Durée en minutes du token
     **/
    private final int MINUTES = 30;

    /**
     * Durée totale du token
     **/
    public final int JWT_EXPIRATION = MINUTES * 60 * 1000;

    public String extractEmail(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    /**
     * Extrait les claims du token
     *
     * @param token   dont extraire les claims
     * @param subject du token
     * @param <T>     le sujet
     * @return les claims
     */
    private <T> T extractClaims(String token, Function<Claims, T> subject) {
        final Claims claims = extractAllClaims(token);
        return subject.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(HashMap<String, Object> claimsExtras, UserDetails userDetails) {
        return buildToken(claimsExtras, userDetails);
    }


    /**
     * Construit un token avec les informations suivantes
     * - clé encryption
     * - claims donnés
     * - date d'expiration
     *
     * @param claimsExtras les claims à mettre dans le token
     * @param userDetails  l'username de l'utilisateur
     * @return un token signé avec son payload
     */
    private String buildToken(
            HashMap<String, Object> claimsExtras,
            UserDetails userDetails
    ) {
        String salt = generateSalt();

        return Jwts
                .builder()
                .setClaims(claimsExtras)

                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (long) EXPIRATION_TOKEN))
                .setId(salt)

                .signWith(getSignedKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Vérifie si le token est valide par rapport à son temps d'expiration et username dans le token.
     *
     * @param token       reçu
     * @param userDetails reçu
     * @return true si le token est valide, false sinon
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractEmail(token);
        return userDetails != null && (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Vérifie si la date d'expiration est passée
     *
     * @param token le token reçu
     * @return true si la date d'expiration est passée sinon false
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Récupère la date d'expiration du token
     *
     * @param token le token dont on veut la date d'expiration
     * @return la date d'expiration
     */
    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    /**
     * Extrait les claims. Vérifie avant si la signature du token est correcte avec la clé d'encryption
     *
     * @param token le token dont on veut les claims
     * @return les claims du token
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().
                setSigningKey(getSignedKey()).
                build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Génération de sel pour varier les tokens par utilisateur. <br>
     * À utiliser pour éviter des tokens similaires lors d'une nouvelle connexion par un même utilisateur
     *
     * @return un string encodé en base64
     */
    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[64];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * @return une clé hashé en hmac256
     */
    private SecretKey getSignedKey() {
        byte[] keyBytes = Decoders.BASE64.decode(ENCRYPTION_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
