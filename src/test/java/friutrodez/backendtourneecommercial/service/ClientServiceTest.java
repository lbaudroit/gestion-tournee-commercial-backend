package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.exception.AdresseInvalideException;
import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.model.Adresse;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Contact;
import friutrodez.backendtourneecommercial.model.Coordonnees;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;


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

    /**
     * Teste la validation de l'adresse du client.
     */
    @Test
    void testCreateValidationClient() {
        Client client = new Client();

        Assertions.assertThrows(DonneesInvalidesException.class, () -> clientService.CreateOneClient(client, "1"));
        client.setNomEntreprise("UneEntreprise");
        Assertions.assertThrows(DonneesInvalidesException.class, () -> clientService.CreateOneClient(client, "1"));
        Adresse adresse = new Adresse("6 Impasse du c", "81490", "Boissezon");

        client.setAdresse(adresse);
        Assertions.assertThrows(DonneesInvalidesException.class, () -> clientService.CreateOneClient(client, "1"));
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

        Client retrievedUser = clientService.CreateOneClient(client, "1");

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

        Assertions.assertThrows(AdresseInvalideException.class,()->clientService.CreateOneClient(client, "1"));
    }


    /**
     * Teste la création de l'utilisateur.
     */
    @Test
    void testEditClient() {
        Client client = new Client();
        client.setNomEntreprise("UneEntreprise");
        client.setContact(new Contact("test", "test", "0102030405"));

        Adresse adress = new Adresse("6 Impasse du Suc", "81490", "Boissezon");

        client.setAdresse(adress);

        Client retrievedClient = clientService.CreateOneClient(client, "1");
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

        Client retrievedClient = clientService.CreateOneClient(client, "1");
        Coordonnees savedCoordinates = retrievedClient.getCoordonnees();
        Client clientEdit = new Client();
        clientEdit.set_id(retrievedClient.get_id());
        clientEdit.setContact(new Contact("test", "test", "0102030405"));

        clientEdit.setNomEntreprise("ModifieClient");
        Adresse adress2 = new Adresse("64 Avenue de Borde", "12000", "Rodez");
        clientEdit.setAdresse(adress2);
        Assertions.assertThrows(AdresseInvalideException.class, ()->clientService.editOneClient(clientEdit.get_id(),clientEdit,"1"));
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
        Assertions.assertThrows(DonneesInvalidesException.class, ()->clientService.editOneClient(clientEdit.get_id(),clientEdit,"1"));
    }
}