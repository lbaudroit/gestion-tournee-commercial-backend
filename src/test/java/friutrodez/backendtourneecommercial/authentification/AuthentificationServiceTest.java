package friutrodez.backendtourneecommercial.authentification;


import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mysql.UtilisateurRepository;
import friutrodez.backendtourneecommercial.service.AuthentificationService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
public class AuthentificationServiceTest {

    @Autowired
    AuthentificationService authentificationService;

    @Test
    @Transactional
    @Rollback
    void testCreationUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setMotDePasse("zaezgr");
        utilisateur.setNom("nomTest");
        utilisateur.setPrenom("prenomTest");

        Utilisateur utilisateurSauvegarde = authentificationService.CreerUnCompte(utilisateur);

        Assertions.assertNotEquals("zaezgr",utilisateurSauvegarde.getMotDePasse(),"Le mot de passe n'a pas été encrypté");
    }

    @Test
    @Transactional
    @Rollback
    void testMotDePasseMauvais() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setMotDePasse("zaezgr");
        utilisateur.setNom("nomTest");
        utilisateur.setPrenom("prenomTest");

        Assertions.assertThrows(Exception.class,()->authentificationService.CreerUnCompte(utilisateur),"Le mot de passe est invalide");

        utilisateur.setMotDePasse("12345678");
        Assertions.assertThrows(Exception.class,()->authentificationService.CreerUnCompte(utilisateur),"Le mot de passe est invalide");

        utilisateur.setMotDePasse("123456aA");
        Assertions.assertThrows(Exception.class,()->authentificationService.CreerUnCompte(utilisateur),"Le mot de passe est invalide");

        utilisateur.setMotDePasse("1234aA.");
        Assertions.assertThrows(Exception.class,()->authentificationService.CreerUnCompte(utilisateur),"Le mot de passe est invalide");


    }

}
