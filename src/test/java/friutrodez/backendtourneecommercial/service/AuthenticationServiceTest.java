package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.exception.DonneesManquantesException;
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
    void testCreationUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setMotDePasse("Ab3@.az234qs");
        utilisateur.setNom("nomTest");
        utilisateur.setPrenom("prenomTest");
        utilisateur.setEmail("Email@email.com");
        utilisateur.setLibelleAdresse("50 Avenue de Bordeaux");

        utilisateur.setCodePostal("12000");
        utilisateur.setVille("Rodez");

        Utilisateur utilisateurSauvegarde = authenticationService.createAnAccount(utilisateur);

        Assertions.assertNotNull(utilisateur.getId(),"L'utilisateur n'a pas été sauvegardé dans la bd");
        Assertions.assertNotEquals("Ab3@.az234",utilisateurSauvegarde.getMotDePasse(),"Le mot de passe n'a pas été encrypté");
    }
    @Test
    void testCreationUtilisateurErreur() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setMotDePasse("Ae@.21ersqds");
        utilisateur.setNom("nomTest");
        utilisateur.setPrenom("prenomTest");
        Assertions.assertThrows(DonneesManquantesException.class,()-> authenticationService.createAnAccount(utilisateur));

        utilisateur.setEmail("   ");
        Assertions.assertThrows(DonneesManquantesException.class,()-> authenticationService.createAnAccount(utilisateur));

        utilisateur.setEmail("Test@te.");
        Assertions.assertThrows(DonneesInvalidesException.class,()-> authenticationService.createAnAccount(utilisateur));

    }

    @Test
    @Transactional
    @Rollback
    void testMotDePasseMauvais() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setMotDePasse("zaezgr");
        utilisateur.setNom("nomTest");
        utilisateur.setPrenom("prenomTest");
        utilisateur.setEmail("Email@mailTest.fr");
        utilisateur.setLibelleAdresse("50 Avenue de Bordeaux");

        utilisateur.setCodePostal("12000");
        utilisateur.setVille("Rodez");

        Assertions.assertThrows(DonneesInvalidesException.class,()-> authenticationService.createAnAccount(utilisateur),"Le mot de passe est invalide");

        utilisateur.setMotDePasse("12345678");
        Assertions.assertThrows(DonneesInvalidesException.class,()-> authenticationService.createAnAccount(utilisateur),"Le mot de passe est invalide");

        utilisateur.setMotDePasse("123456aA");
        Assertions.assertThrows(DonneesInvalidesException.class,()-> authenticationService.createAnAccount(utilisateur),"Le mot de passe est invalide");

        utilisateur.setMotDePasse("1234aA.");
        Assertions.assertThrows(DonneesInvalidesException.class,()-> authenticationService.createAnAccount(utilisateur),"Le mot de passe est invalide");


    }

}
