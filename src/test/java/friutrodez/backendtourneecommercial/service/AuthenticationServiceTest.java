package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.dto.DonneesAuthentification;
import friutrodez.backendtourneecommercial.exception.AdresseInvalideException;
import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mysql.UtilisateurRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.Rollback;


/**
 * Classe de test pour AuthenticationServiceTest.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
@Transactional
@Rollback
@SpringBootTest
public class AuthenticationServiceTest {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    UtilisateurRepository utilisateurRepository;

    /**
     * Teste la création de l'utilisateur.
     */
    @Test
    void testBuildUser() {
        Utilisateur createdUser = createUser();
        Utilisateur foundUser = utilisateurRepository.findByEmail("Email@email.com");
        Assertions.assertNotNull(foundUser);

        Assertions.assertNotNull(createdUser.getId(), "L'utilisateur n'a pas été sauvegardé dans la bd");
        Assertions.assertNotEquals("Ab3@.az234", createdUser.getMotDePasse(), "Le mot de passe n'a pas été encrypté");
    }

    /**
     * Teste la création de l'utilisateur avec de mauvais email.
     */
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

    /**
     * Teste la création de l'utilisateur avec de mauvais mot de passe.
     */
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

    /**
     * Teste l'authentification de l'utilisateur.
     */
    @Test
    void testTryAuthenticate() {
        Utilisateur user = createUser();
        Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());

        Assertions.assertDoesNotThrow(()->authenticationService.tryAuthenticate(new DonneesAuthentification("Email@email.com","Ab3@.az234qs")));
        Assertions.assertEquals(SecurityContextHolder.getContext().getAuthentication().getPrincipal(),user);
        Assertions.assertEquals(authenticationService.loadUserDetails("Email@email.com"),user);
    }

    /**
     * Teste l'authentification de l'utilisateur à partir de requête.
     */
    @Test
    void testTryAuthenticateWithRequest() {
        Utilisateur user = createUser();
        Assertions.assertEquals(authenticationService.loadUserDetails("Email@email.com"),user);
        UserDetails userDetails = authenticationService.loadUserDetails("Email@email.com");

        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mockRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
        Assertions.assertEquals(authenticationService.tryAuthenticateWithRequest(userDetails,mockRequest),user);
        Assertions.assertEquals(SecurityContextHolder.getContext().getAuthentication().getPrincipal(),user);
    }

    /**
     * Méthode outil pour créer un utilisateur.
     */
    private Utilisateur createUser() {
        Utilisateur user = new Utilisateur();
        user.setMotDePasse("Ab3@.az234qs");
        user.setNom("nomTest");
        user.setPrenom("prenomTest");
        user.setEmail("Email@email.com");

        user.setLibelleAdresse("50 Avenue de Bordeaux");
        user.setCodePostal("12000");
        user.setVille("Rodez");

        return authenticationService.createAnAccount(user);
    }

    /**
     * Teste la modification de l'utiliseur et de ses erreurs potentielles.
     */
    @Test
    void testEditAccount() {
        Utilisateur user = createUser();
        authenticationService.tryAuthenticate(new DonneesAuthentification(user.getEmail(),"Ab3@.az234qs"));
        Utilisateur editUser = Utilisateur.builder().nom(user.getNom())
                .email("NewEmail@email.com").libelleAdresse(user.getLibelleAdresse())
                .ville(user.getVille()).prenom("UnPrenom").nom("UnNom")
                .codePostal(user.getCodePostal()).motDePasse("Ab3@.az234qs").id(user.getId()).build();

        Utilisateur editedUser = authenticationService.editAnAccount(editUser);
        UserDetails userDetails = authenticationService.loadUserDetails("Email@email.com");
        Assertions.assertNull(userDetails);

        // L'utilisateur même après le changement de ces informations reste authentifié.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertNotNull(authentication);

        Assertions.assertNotEquals("Email@email.com",editedUser.getEmail());

        editUser.setLibelleAdresse("12 Avenue de Bordeaux");
        Assertions.assertDoesNotThrow(()->authenticationService.editAnAccount(editUser));
        editUser.setLibelleAdresse("12 Avenue de Bordeau");
        Assertions.assertThrows(AdresseInvalideException.class,()->authenticationService.editAnAccount(editUser));
    }

}
