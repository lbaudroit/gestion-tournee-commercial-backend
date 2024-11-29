package friutrodez.backendtourneecommercial.repository;

import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ClientRepositoryTest {

    @Autowired
    ClientRepository clientRepository;
    @Test
    void testCreation(){
        Client client = new Client();
        client.setNomEntreprise("entreprise1");
        client.setDescriptif("Une entreprise");

        clientRepository.sauvegarder(client);

        Assert.assertTrue("Le client n'a pas été créé",
                clientRepository.existe("nomEntreprise",
                        "entreprise1",Client.class));
        
        clientRepository.enlever("nomEntreprise","entreprise1",Client.class);
    }
}
