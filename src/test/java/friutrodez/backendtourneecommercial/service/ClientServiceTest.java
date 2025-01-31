package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.exception.DonneesManquantesException;
import friutrodez.backendtourneecommercial.model.Adresse;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClientServiceTest {


    @Autowired
    ClientService clientService;
    @BeforeEach
    void setUp() {
    }

    @Test
    void CreerUnCompteVerificationTest() {
        Client client = new Client();

        Assertions.assertThrows(DonneesManquantesException.class,()-> clientService.creerUnClient(client,"1"));
        client.setNomEntreprise("UneEntreprise");
        Assertions.assertThrows(DonneesManquantesException.class,() ->clientService.creerUnClient(client,"1"));
        Adresse adresse = new Adresse("6 Impasse du c","81490","Boissezon");

        client.setAdresse(adresse);
        Assertions.assertThrows(DonneesInvalidesException.class,() -> clientService.creerUnClient(client,"1"));
    }

    @Test
    void CreerUnCompteTest() {
        Client client = new Client();
        client.setNomEntreprise("UneEntreprise");
        Adresse adresse = new Adresse("6 Impasse du Suc","81490","Boissezon");

        client.setAdresse(adresse);
        Client clientRecupere = clientService.creerUnClient(client,"1");

        Assertions.assertEquals(clientRecupere.getIdUtilisateur(),"1");

        Assertions.assertNotNull(clientRecupere.getCoordonnees());
    };
}