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
    AuthentificationService authentificationService;

    @Test
    void testCreationUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setMotDePasse("zaezgr");
        utilisateur.setNom("nomTest");
        utilisateur.setPrenom("prenomTest");
        utilisateur.setEmail("Email@email.com");

        Utilisateur utilisateurSauvegarde = authentificationService.creerUnCompte(utilisateur);

        Assertions.assertNotEquals("zaezgr",utilisateurSauvegarde.getMotDePasse(),"Le mot de passe n'a pas été encrypté");
    }
    @Test
    void testCreationUtilisateurErreur() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setMotDePasse("Ae@.21er");
        utilisateur.setNom("nomTest");
        utilisateur.setPrenom("prenomTest");
        Assertions.assertThrows(DonneesManquantesException.class,()-> authentificationService.creerUnCompte(utilisateur));

        utilisateur.setEmail("   ");
        Assertions.assertThrows(DonneesManquantesException.class,()-> authentificationService.creerUnCompte(utilisateur));

        utilisateur.setEmail("Test@te.");
        Assertions.assertThrows(DonneesInvalidesException.class,()-> authentificationService.creerUnCompte(utilisateur));

    }

    @Test
    @Transactional
    @Rollback
    void testMotDePasseMauvais() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setMotDePasse("zaezgr");
        utilisateur.setNom("nomTest");
        utilisateur.setPrenom("prenomTest");

        //Assertions.assertThrows(Exception.class,()->authentificationService.creerUnCompte(utilisateur),"Le mot de passe est invalide");

        utilisateur.setMotDePasse("12345678");
        //Assertions.assertThrows(Exception.class,()->authentificationService.creerUnCompte(utilisateur),"Le mot de passe est invalide");

        utilisateur.setMotDePasse("123456aA");
        //Assertions.assertThrows(Exception.class,()->authentificationService.creerUnCompte(utilisateur),"Le mot de passe est invalide");

        utilisateur.setMotDePasse("1234aA.");
        //Assertions.assertThrows(Exception.class,()->authentificationService.creerUnCompte(utilisateur),"Le mot de passe est invalide");


    }

}
