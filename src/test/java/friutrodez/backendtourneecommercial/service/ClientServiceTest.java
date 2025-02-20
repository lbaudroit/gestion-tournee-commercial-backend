package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.exception.AdresseInvalideException;
import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.helper.ConfigurationSecurityContextTest;
import friutrodez.backendtourneecommercial.model.*;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import friutrodez.backendtourneecommercial.repository.mysql.AppartientRepository;
import friutrodez.backendtourneecommercial.repository.mysql.ItineraireRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Classe de test pour ClientServiceTest.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
@Transactional
@Rollback
@SpringBootTest
class ClientServiceTest {

    @Autowired
    ClientService clientService;

    /**
     * Utilisé seulement pour sauvegarder un client avec une mauvaise adresse
     */
    @Autowired
    ClientMongoTemplate clientMongoTemplate;

    @Autowired
    AppartientRepository appartientRepository;

    @Autowired
    ItineraireRepository itineraireRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ConfigurationSecurityContextTest configurationSecurityContextTest;

    /**
     * Teste la validation de l'adresse du client.
     */
    @Test
    void testCreateValidationClient() {
        Client client = new Client();

        assertThrows(DonneesInvalidesException.class, () -> clientService.createOneClient(client, "1"));
        client.setNomEntreprise("UneEntreprise");
        assertThrows(DonneesInvalidesException.class, () -> clientService.createOneClient(client, "1"));
        Adresse adresse = new Adresse("6 Impasse du c", "81490", "Boissezon");

        client.setAdresse(adresse);
        assertThrows(DonneesInvalidesException.class, () -> clientService.createOneClient(client, "1"));
    }

    /**
     * Teste la création du client.
     */
    @Test
    void testCreateClient() {
        Client client = new Client();
        client.setNomEntreprise("UneEntreprise");
        client.setContact(new Contact("test", "test", "0102030405"));

        Adresse adress = new Adresse("6 Impasse du Suc", "81490", "Boissezon");

        client.setAdresse(adress);

        Client retrievedUser = clientService.createOneClient(client, "1");

        Assertions.assertEquals(retrievedUser.getIdUtilisateur(), "1");

        Assertions.assertNotNull(retrievedUser.getCoordonnees());
    }
    /**
     * Teste la création du client avec des erreurs.
     */
    @Test
    void testCreateClientWrong() {
        Client client = new Client();
        client.setNomEntreprise("UneEntreprise");
        client.setContact(new Contact("test", "test", "0102030405"));

        Adresse adress = new Adresse("6 Impasse du ", "81490", "Boissezon");

        client.setAdresse(adress);

        assertThrows(AdresseInvalideException.class,()->clientService.createOneClient(client, "1"));
    }


    /**
     * Teste la création de l'utilisateur.
     */
    @Test
    void testEditClient() {
        Client client = new Client();
        client.setNomEntreprise("UneEntreprise");
        client.setContact(new Contact("test", "test", "0102030405"));

        Adresse address = new Adresse("6 Impasse du Suc", "81490", "Boissezon");

        client.setAdresse(address);
        Client clientRecupere = clientService.createOneClient(client, "1");
        client.setAdresse(address);

        Client retrievedClient = clientService.createOneClient(client, "1");
        Coordonnees savedCoordinates = retrievedClient.getCoordonnees();
        Client clientEdit = new Client();
        clientEdit.set_id(retrievedClient.get_id());
        clientEdit.setContact(new Contact("test", "test", "0102030405"));

        clientEdit.setNomEntreprise("ModifieClient");
        Adresse adress2 = new Adresse("64 Avenue de Bordeaux", "12000", "Rodez");
        clientEdit.setAdresse(adress2);
        clientService.editOneClient(clientEdit.get_id(),clientEdit,"1");

        Assertions.assertNotEquals(savedCoordinates,clientEdit.getCoordonnees());
        Assertions.assertNotEquals(retrievedClient.getNomEntreprise(),clientEdit.getNomEntreprise());
    }

    /**
     * Teste la modification du client avec une adresse mauvaise.
     */
    @Test
    void testEditClientWrong() {
        Client client = new Client();
        client.setNomEntreprise("UneEntreprise");
        client.setContact(new Contact("test", "test", "0102030405"));

        Adresse adress = new Adresse("6 Impasse du Suc", "81490", "Boissezon");

        client.setAdresse(adress);

        Client retrievedClient = clientService.createOneClient(client, "1");
        Coordonnees savedCoordinates = retrievedClient.getCoordonnees();
        Client clientEdit = new Client();
        clientEdit.set_id(retrievedClient.get_id());
        clientEdit.setContact(new Contact("test", "test", "0102030405"));

        clientEdit.setNomEntreprise("ModifieClient");
        Adresse adress2 = new Adresse("64 Avenue de Borde", "12000", "Rodez");
        clientEdit.setAdresse(adress2);
        assertThrows(AdresseInvalideException.class, ()->clientService.editOneClient(clientEdit.get_id(),clientEdit,"1"));
    }

    /**
     * Teste la modification d'un client avec un ID invalide.
     */
    @Test
    void testEditClientInvalidId() {
        Client clientEdit = new Client();
        clientEdit.setNomEntreprise("ModifieClient");
        clientEdit.setContact(new Contact("test", "test", "0102030405"));
        Adresse adress = new Adresse("64 Avenue de Bordeaux", "12000", "Rodez");
        clientEdit.setAdresse(adress);

        assertThrows(NoSuchElementException.class, () -> clientService.editOneClient("invalidId", clientEdit, "1"));
    }

    /**
     * Teste la modification d'un client sauvegardé sans adresse.
     */
    @Test
    void testEditClientNoAdress() {
        Client client = new Client();
        client.setNomEntreprise("UneEntreprise");
        client.setContact(new Contact("test", "test", "0102030405"));
        client.setIdUtilisateur("1");
        Adresse adress = new Adresse("", "", "");

        client.setAdresse(adress);
        clientMongoTemplate.save(client);

        Client clientEdit = client;
        clientEdit.setNomEntreprise("Modif");
        assertThrows(DonneesInvalidesException.class, ()->clientService.editOneClient(clientEdit.get_id(),clientEdit,"1"));
    }

    /**
     * Teste la suppression d'un client avec des données valides.
     */
    @Test
    void deleteOneClientWithValidData() {
        Utilisateur user = createUser();
        Client client = createClient(user);
        Itineraire itineraire = createItineraire(user, client);

        assertDoesNotThrow(() -> clientService.deleteOneClient(client.get_id(), user));

        assertTrue(clientMongoTemplate.getOneClient(client.get_id(), String.valueOf(user.getId())).isEmpty());
        assertTrue(appartientRepository.findAllByIdEmbedded_ClientId(client.get_id()).isEmpty());
        assertFalse(itineraireRepository.existsById(itineraire.getId()));
    }

    /**
     * Teste la suppression d'un client avec un ID de client invalide.
     */
    @Test
    void deleteOneClientWithInvalidClientIdThrowsException() {
        Utilisateur user = createUser();
        String invalidClientId = "invalidId";

        assertThrows(NoSuchElementException.class, () -> clientService.deleteOneClient(invalidClientId, user));
    }

    /**
     * Teste la suppression d'un client avec un utilisateur non existant.
     */
    @Test
    void deleteOneClientWithNonExistentUserThrowsException() {
        Utilisateur nonExistentUser = new Utilisateur();
        nonExistentUser.setId(999L); // Supposons que 999 est un ID non existant
        Client client = createClient(createUser());

        assertThrows(NoSuchElementException.class, () -> clientService.deleteOneClient(client.get_id(), nonExistentUser));
    }

    @Test
    void testGetAllProspectsAroundAPoint() {
        Utilisateur user = configurationSecurityContextTest.getMockUser();
        Client client = new Client();
        client.setNomEntreprise("UneEntreprise");

        client.setContact(new Contact("test", "test", "0102030405"));
        Adresse adress = new Adresse("2 Route d'Espalion", "12850", "Onet-le-Château");
        client.setAdresse(adress);
        Client client2 = new Client();
        client2.setNomEntreprise("UneEntreprise2");
        client2.setContact(new Contact("test", "test", "0102030405"));
        Adresse adress2 = new Adresse("87 Avenue de Paris", "12000", "Rodez");

        client.setAdresse(adress2);
        Client client3 = new Client();
        client3.setNomEntreprise("UneEntreprise3");
        Adresse adress3 = new Adresse("31 Avenue du Marechal Joffre", "12000", "Rodez");

        client3.setContact(new Contact("test", "test", "0102030405"));
        client2.setAdresse(adress2);
        client3.setAdresse(adress3);

        Client notProspectClient = new Client();
        notProspectClient.setNomEntreprise("UneEntreprise3");

        notProspectClient.setContact(new Contact("test", "test", "0102030405"));
        notProspectClient.setAdresse(adress3);
        notProspectClient.setClientEffectif(true);

        Client farAdressClient = new Client();
        farAdressClient.setNomEntreprise("UneEntreprise3");

        farAdressClient.setContact(new Contact("test", "test", "0102030405"));

        Adresse paris = new Adresse("22 Rue Paul Vaillant-couturier", "92140", "Clamart");

        farAdressClient.setAdresse(paris);

        clientService.createOneClient(client,String.valueOf(user.getId()));
        clientService.createOneClient(client2,String.valueOf(user.getId()));
        clientService.createOneClient(client3,String.valueOf(user.getId()));
        clientService.createOneClient(notProspectClient,String.valueOf(user.getId()));
        clientService.createOneClient(farAdressClient,String.valueOf(user.getId()));

        // Point est un point proche de la gare de rodez
        Coordonnees point = new Coordonnees(44.36208,2.580071);
        Assertions.assertEquals(3,clientService.getAllProspectsAround(point, String.valueOf(user.getId())).size());
        clientMongoTemplate.mongoTemplate.remove(client3);
        clientMongoTemplate.mongoTemplate.remove(client2);
        clientMongoTemplate.mongoTemplate.remove(client);
    }

    @Test
    void testNoProspectsAround() {
        Utilisateur user = configurationSecurityContextTest.getMockUser();
        Coordonnees point2 = new Coordonnees(0,0);
        Assertions.assertEquals(0,clientService.getAllProspectsAround(point2, String.valueOf(user.getId())).size());


    }

    /**
     * Crée un utilisateur pour les tests.
     *
     * @return un nouvel utilisateur
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
     * Crée un client pour les tests.
     *
     * @param user l'utilisateur associé au client
     * @return un nouveau client
     */
    private Client createClient(Utilisateur user) {
        Client client = new Client();
        client.setNomEntreprise("UneEntreprise");
        client.setContact(new Contact("test", "test", "0102030405"));
        Adresse address = new Adresse("6 Impasse du Suc", "81490", "Boissezon");
        client.setAdresse(address);
        return clientService.createOneClient(client, String.valueOf(user.getId()));
    }

    /**
     * Crée un itinéraire pour les tests.
     *
     * @param user l'utilisateur associé à l'itinéraire
     * @param client le client associé à l'itinéraire
     * @return un nouvel itinéraire
     */
    private Itineraire createItineraire(Utilisateur user, Client client) {
        Itineraire itineraire = new Itineraire();
        itineraire.setUtilisateur(user);
        itineraire.setNom("Test Itineraire");
        itineraire.setDistance(100);
        itineraireRepository.save(itineraire);

        Appartient appartient = new Appartient();
        appartient.setIdEmbedded(new AppartientKey(itineraire, client.get_id()));
        appartientRepository.save(appartient);

        return itineraire;
    }
}