package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.dto.DonneesAuthentification;
import friutrodez.backendtourneecommercial.exception.AdresseInvalideException;
import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.model.Adresse;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;


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
    PasswordEncoder passwordEncoder;

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
        assertNotEquals("Ab3@.az234", createdUser.getMotDePasse(), "Le mot de passe n'a pas été encrypté");
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
        assertThrows(DonneesInvalidesException.class, () -> authenticationService.createAnAccount(user));

        user.setEmail("   ");
        assertThrows(DonneesInvalidesException.class, () -> authenticationService.createAnAccount(user));

        user.setEmail("Test@te.");
        assertThrows(DonneesInvalidesException.class, () -> authenticationService.createAnAccount(user));

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

        assertThrows(DonneesInvalidesException.class, () -> authenticationService.createAnAccount(user), "Le mot de passe est invalide");

        user.setMotDePasse("12345678");
        assertThrows(DonneesInvalidesException.class, () -> authenticationService.createAnAccount(user), "Le mot de passe est invalide");

        user.setMotDePasse("123456aA");
        assertThrows(DonneesInvalidesException.class, () -> authenticationService.createAnAccount(user), "Le mot de passe est invalide");

        user.setMotDePasse("1234aA.");
        assertThrows(DonneesInvalidesException.class, () -> authenticationService.createAnAccount(user), "Le mot de passe est invalide");
    }

    /**
     * Teste l'authentification de l'utilisateur.
     */
    @Test
    void testTryAuthenticate() {
        Utilisateur user = createUser();
        Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());

        assertDoesNotThrow(()->authenticationService.tryAuthenticate(new DonneesAuthentification("Email@email.com","Ab3@.az234qs")));
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

        assertNotEquals("Email@email.com",editedUser.getEmail());

        editUser.setLibelleAdresse("12 Avenue de Bordeaux");
        assertDoesNotThrow(()->authenticationService.editAnAccount(editUser));
        editUser.setLibelleAdresse("12 Avenue de Bordeau");
        assertThrows(AdresseInvalideException.class,()->authenticationService.editAnAccount(editUser));
    }

    /**
     * Vérifie une adresse valide.
     */
    @Test
    void checkAddressWithValidAddress() {
        Adresse validAddress = new Adresse("50 Avenue de Bordeaux", "12000", "Rodez");
        assertDoesNotThrow(() -> authenticationService.checkAddress(validAddress));
    }
    /**
     * Vérifie qu'une adresse invalide lance une exception.
     */
    @Test
    void checkAddressWithInvalidAddressThrowsException() {
        Adresse invalidAddress = new Adresse("Invalid Address", "12345", "InvalidCity");
        assertThrows(AdresseInvalideException.class, () -> authenticationService.checkAddress(invalidAddress));
    }

    /**
     * Vérifie un mot de passe valide.
     */
    @Test
    void checkPasswordWithValidPassword() {
        String validPassword = "ValidPass123_";
        assertDoesNotThrow(() -> authenticationService.checkPassword(validPassword));
    }

    /**
     * Vérifie qu'un mot de passe invalide lance une exception.
     */
    @Test
    void checkPasswordWithInvalidPasswordThrowsException() {
        String invalidPassword = "short";
        assertThrows(DonneesInvalidesException.class, () -> authenticationService.checkPassword(invalidPassword));
    }

    /**
     * Modifie le mot de passe avec des données valides.
     */
    @Test
    void editPasswordWithValidData() {
        Utilisateur user = createUser();
        String newPassword = "NewValidPass123_";

        Utilisateur updatedUser = authenticationService.editPassword(user, newPassword);
        assertNotEquals("Ab3@.az234qs", updatedUser.getMotDePasse());
        assertTrue(passwordEncoder.matches(newPassword, updatedUser.getMotDePasse()));
    }

    /**
     * Vérifie que la modification du mot de passe avec des données invalides lance une exception.
     */
    @Test
    void editPasswordWithInvalidDataThrowsException() {
        Utilisateur user = createUser();
        String invalidPassword = "short";

        assertThrows(DonneesInvalidesException.class, () -> authenticationService.editPassword(user, invalidPassword));
    }

    /**
     * Vérifie que la modification du mot de passe pour un utilisateur inexistant lance une exception.
     */
    @Test
    void editPasswordWithNonExistentUserThrowsException() {
        Utilisateur nonExistentUser = new Utilisateur();
        nonExistentUser.setId(999L); // Assuming 999 is a non-existent ID
        String newPassword = "NewValidPass123_";

        assertThrows(NoSuchElementException.class, () -> authenticationService.editPassword(nonExistentUser, newPassword));
    }
}