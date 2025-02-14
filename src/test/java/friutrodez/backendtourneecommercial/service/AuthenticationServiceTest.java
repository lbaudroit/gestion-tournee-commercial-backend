package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@Transactional
@Rollback
@SpringBootTest
public class AuthenticationServiceTest {

    @Autowired
    AuthenticationService authenticationService;

    @Test
    void testBuildUser() {
        Utilisateur user = new Utilisateur();
        user.setMotDePasse("Ab3@.az234qs");
        user.setNom("nomTest");
        user.setPrenom("prenomTest");
        user.setEmail("Email@email.com");

        user.setLibelleAdresse("50 Avenue de Bordeaux");
        user.setCodePostal("12000");
        user.setVille("Rodez");

        Utilisateur utilisateurSauvegarde = authenticationService.createAnAccount(user);

        Assertions.assertNotNull(user.getId(), "L'utilisateur n'a pas été sauvegardé dans la bd");
        Assertions.assertNotEquals("Ab3@.az234", utilisateurSauvegarde.getMotDePasse(), "Le mot de passe n'a pas été encrypté");
    }

    @Test
    void testCreationUserFail() {
        Utilisateur user = new Utilisateur();
        user.setMotDePasse("Ae@.21ersqds");
        user.setNom("nomTest");
        user.setPrenom("prenomTest");
        Assertions.assertThrows(DonneesInvalidesException.class, () -> authenticationService.createAnAccount(user));

        user.setEmail("   ");
        Assertions.assertThrows(DonneesInvalidesException.class, () -> authenticationService.createAnAccount(user));

        user.setEmail("Test@te.");
        Assertions.assertThrows(DonneesInvalidesException.class, () -> authenticationService.createAnAccount(user));

    }

    @Test
    @Transactional
    @Rollback
    void testWrongPassword() {
        Utilisateur user = new Utilisateur();
        user.setMotDePasse("zaezgr");
        user.setNom("nomTest");
        user.setPrenom("prenomTest");
        user.setEmail("Email@mailTest.fr");
        user.setLibelleAdresse("50 Avenue de Bordeaux");

        user.setCodePostal("12000");
        user.setVille("Rodez");

        Assertions.assertThrows(DonneesInvalidesException.class, () -> authenticationService.createAnAccount(user), "Le mot de passe est invalide");

        user.setMotDePasse("12345678");
        Assertions.assertThrows(DonneesInvalidesException.class, () -> authenticationService.createAnAccount(user), "Le mot de passe est invalide");

        user.setMotDePasse("123456aA");
        Assertions.assertThrows(DonneesInvalidesException.class, () -> authenticationService.createAnAccount(user), "Le mot de passe est invalide");

        user.setMotDePasse("1234aA.");
        Assertions.assertThrows(DonneesInvalidesException.class, () -> authenticationService.createAnAccount(user), "Le mot de passe est invalide");


    }

}
