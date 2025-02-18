package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.helper.ConfigurationSecurityContextTest;
import friutrodez.backendtourneecommercial.model.*;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@Transactional
@Rollback
@SpringBootTest
class ClientServiceTest {


    @Autowired
    ClientService clientService;

    @Autowired
    ClientMongoTemplate clientMongoTemplate;

    @Autowired
    ConfigurationSecurityContextTest configurationSecurityContextTest;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateValidationClient() {
        Client client = new Client();

        Assertions.assertThrows(DonneesInvalidesException.class, () -> clientService.createOneClient(client, "1"));
        client.setNomEntreprise("UneEntreprise");
        Assertions.assertThrows(DonneesInvalidesException.class, () -> clientService.createOneClient(client, "1"));
        Adresse adresse = new Adresse("6 Impasse du c", "81490", "Boissezon");

        client.setAdresse(adresse);
        Assertions.assertThrows(DonneesInvalidesException.class, () -> clientService.createOneClient(client, "1"));
    }

    @Test
    void testCreateClient() {
        Client client = new Client();
        client.setNomEntreprise("UneEntreprise");
        client.setContact(new Contact("test", "test", "0102030405"));

        Adresse adresse = new Adresse("6 Impasse du Suc", "81490", "Boissezon");

        client.setAdresse(adresse);
        Client clientRecupere = clientService.createOneClient(client, "1");

        Assertions.assertEquals(clientRecupere.getIdUtilisateur(), "1");

        Assertions.assertNotNull(clientRecupere.getCoordonnees());
        clientMongoTemplate.mongoTemplate.remove(clientRecupere);
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
}