package friutrodez.backendtourneecommercial.repository;

import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ClientRepositoryTest {

    @Autowired
    ClientMongoTemplate clientRepository;
    @Test
    void testCreation(){
        Client client = new Client();
        client.setNomEntreprise("entreprise1");
        client.setDescriptif("Une entreprise");

        clientRepository.sauvegarder(client);

        Assertions.assertTrue(clientRepository.existe("nomEntreprise",
                "entreprise1"),"Le client n'a pas été créé");

        
        clientRepository.enlever("nomEntreprise","entreprise1");
    }


}
