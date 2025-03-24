package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.model.Utilisateur;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;

/**
 * Classe de test pour JwtServiceTests.
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
public class JwtServiceTest {


    /**
     * Teste la génération d'un token.
     */
    @Test
    void testBuild() {
        JwtService jwtService = new JwtService();
        Utilisateur userDetails = new Utilisateur();
        userDetails.setMotDePasse("123");
        userDetails.setNom("testNom");
        userDetails.setEmail("mailTest@test.fr");

        String token = jwtService.generateToken(new HashMap<>(), userDetails);
        Assertions.assertEquals("mailTest@test.fr", jwtService.extractEmail(token)
                , "extraireEmail ne retourne pas l'email dans le token");

        Assertions.assertTrue(jwtService.isTokenValid(token, userDetails), "Le token n'est pas valide après sa création");
        Assertions.assertFalse(jwtService.isTokenExpired(token), "Le token ne doit pas être expiré après sa création");
        String token2 = jwtService.generateToken(new HashMap<>(), userDetails);

        Assertions.assertNotEquals(token2, token, "Le token ne doit pas être le même après une nouvelle génération du token");

    }

    /**
     * Teste l'extraction des données du token.
     */
    @Test
    void testExtractClaims() {
        JwtService jwtService = new JwtService();
        Utilisateur userDetails = new Utilisateur();

        userDetails.setMotDePasse("123");
        userDetails.setNom("testNom");
        userDetails.setEmail("testEmail@e.e");
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("id", 1);

        String token = jwtService.generateToken(claims, userDetails);
        Claims claims1 = jwtService.extractAllClaims(token);
        Assertions.assertEquals(1, claims1.get("id")
                , "extraireTousClaims ne renvoient pas l'id correcte");

        Assertions.assertEquals("testEmail@e.e", jwtService.extractEmail(token));

    }

    /**
     * Teste l'extraction de l'email dans le token'.
     */
    @Test
    void testExtractEmail() {
        JwtService jwtService = new JwtService();
        Utilisateur userDetails = new Utilisateur();

        userDetails.setMotDePasse("123");
        userDetails.setNom("testNom");
        userDetails.setEmail("testEmail@e.e");

        String token = jwtService.generateToken(userDetails);

        Assertions.assertEquals("testEmail@e.e", jwtService.extractEmail(token));
    }

    /**
     * Teste l'extraction de la date d'expiration du token'.
     */
    @Test
    void testExtractExpiration() {
        JwtService jwtService = new JwtService();
        Utilisateur userDetails = new Utilisateur();

        userDetails.setMotDePasse("123");
        userDetails.setNom("testNom");
        userDetails.setEmail("testEmail@e.e");

        String token = jwtService.generateToken(userDetails);

        Assertions.assertEquals(Date.from(Instant.now().plus(30, ChronoUnit.MINUTES)).toString(), jwtService.extractExpiration(token).toString());
    }

    /**
     * Teste l'expiration du token.
     */
    @Test
    void testExpired() {
        JwtService jwtService = new JwtService();
        Utilisateur userDetails = new Utilisateur();

        userDetails.setMotDePasse("123");
        userDetails.setNom("testNom");
        userDetails.setEmail("testEmail@e.e");

        String token = jwtService.generateToken(userDetails);
        Assertions.assertFalse(jwtService.isTokenExpired(token));

        // TODO test : token expired true
    }

    /**
     * Teste la validité du token.
     */
    @Test
    void testValid() {
        JwtService jwtService = new JwtService();
        Utilisateur userDetails = new Utilisateur();

        userDetails.setMotDePasse("123");
        userDetails.setNom("testNom");
        userDetails.setEmail("testEmail@e.e");

        String token = jwtService.generateToken(userDetails);
        Assertions.assertTrue(jwtService.isTokenValid(token, userDetails));
        Assertions.assertFalse(jwtService.isTokenValid(token, null));

        Utilisateur userDetails2 = new Utilisateur();
        userDetails2.setMotDePasse("ersgfh");
        userDetails2.setEmail("sdfghj");
        Assertions.assertFalse(jwtService.isTokenValid(token, userDetails2));
    }
}