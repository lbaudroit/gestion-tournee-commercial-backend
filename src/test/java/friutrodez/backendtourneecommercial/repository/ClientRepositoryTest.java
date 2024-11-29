package friutrodez.backendtourneecommercial.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
public class ClientRepositoryTest {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    public MongoTemplate mongoTemplate;
    @Test
    void testCreation(){
        Client client = new Client();
        client.setNomEntreprise("fsdgfd");
        client.setDescriptif("fsddg");

        mongoTemplate.save(client);

        Assert.assertTrue("Le client n'a pas été créé",clientRepository.existe("nomEntreprise","fsdgfd",Client.class));
        clientRepository.enlever("nomEntreprise","fsdgfd",Client.class);


    }
}
