package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.model.Adresse;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Contact;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ClientServiceTest {


    @Autowired
    ClientService clientService;
    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateValidationClient() {
        Client client = new Client();

        Assertions.assertThrows(DonneesInvalidesException.class,()-> clientService.CreateOneClient(client,"1"));
        client.setNomEntreprise("UneEntreprise");
        Assertions.assertThrows(DonneesInvalidesException.class,() ->clientService.CreateOneClient(client,"1"));
        Adresse adresse = new Adresse("6 Impasse du c","81490","Boissezon");

        client.setAdresse(adresse);
        Assertions.assertThrows(DonneesInvalidesException.class,() -> clientService.CreateOneClient(client,"1"));
    }

    @Test
    void testCreateClient() {
        Client client = new Client();
        client.setNomEntreprise("UneEntreprise");
        client.setContact(new Contact("test","test","0102030405"));

        Adresse adresse = new Adresse("6 Impasse du Suc","81490","Boissezon");

        client.setAdresse(adresse);
        Client clientRecupere = clientService.CreateOneClient(client,"1");

        Assertions.assertEquals(clientRecupere.getIdUtilisateur(),"1");

        Assertions.assertNotNull(clientRecupere.getCoordonnees());
    };
}