package friutrodez.backendtourneecommercial.authentification;

import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.service.JwtService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;

public class JwtServiceTest {


    @Test
    void testConstruction() {
        JwtService jwtService = new JwtService();
        Utilisateur userDetails = new Utilisateur();
        userDetails.setMotDePasse("123");
        userDetails.setNom("testNom");

        String token = jwtService.genererToken(new HashMap<>(),userDetails);
        Assertions.assertEquals(jwtService.extraireNomUtilisateur(token),"testNom"
                ,"extraireNomUtilisateur ne retourne pas le nom dans le token");

        Assertions.assertTrue(jwtService.tokenEstValide(token,userDetails),"Le token n'est pas valide après sa création");
        Assertions.assertFalse(jwtService.tokenEstExpire(token),"Le token ne doit pas être expiré après sa création");
        String token2 = jwtService.genererToken(new HashMap<>(),userDetails);

        Assertions.assertNotEquals(token2,token,"Le token ne doit pas être le même après une nouvelle génération du token");

    }

    @Test
    void testRecuperationClaims() {
        JwtService jwtService = new JwtService();
        Utilisateur userDetails = new Utilisateur();

        userDetails.setMotDePasse("123");
        userDetails.setNom("testNom");
        HashMap<String,Object> claims = new HashMap<>();
        claims.put("id",1);

        String token = jwtService.genererToken(claims,userDetails);
        Claims claims1 = jwtService.extraireTousClaims(token);
        Assertions.assertEquals(claims1.get("id"),1
                ,"extraireTousClaims ne renvoient pas l'id correcte");

    }
}
