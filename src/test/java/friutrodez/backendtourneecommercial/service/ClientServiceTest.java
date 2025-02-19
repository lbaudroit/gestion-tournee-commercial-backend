package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.model.Adresse;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Contact;
import friutrodez.backendtourneecommercial.model.Coordonnees;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ClientServiceTest {

    @Autowired
    ClientService clientService;

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

}