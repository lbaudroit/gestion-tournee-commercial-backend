package friutrodez.backendtourneecommercial.model;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClientTest {
    @Test
    void testEquals() {
        Client client = new Client();

        client.setNomEntreprise("Nom");
        Client clientAComparer= new Client();
        clientAComparer.setNomEntreprise("PasNom");

        Assertions.assertNotEquals(client,clientAComparer);

        client.setCoordonnees(new Coordonnees(20,20));
        clientAComparer.setCoordonnees(new Coordonnees(20,20));

        Assertions.assertEquals(client,clientAComparer);

        client.setCoordonnees(new Coordonnees(10,20));
        Assertions.assertNotEquals(client,clientAComparer);
    }
}
