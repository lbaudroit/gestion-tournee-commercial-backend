package friutrodez.backendtourneecommercial.repository;

import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ClientRepositoryTest {

    @Autowired
    ClientMongoTemplate clientMongoTemplate;
    @Test
    void testCreation(){
        Client client = new Client();
        client.setNomEntreprise("entreprise1");
        client.setDescriptif("Une entreprise");

        clientMongoTemplate.sauvegarder(client);

        Assertions.assertTrue(clientMongoTemplate.existe("nomEntreprise",
                "entreprise1"),"Le client n'a pas été créé");

        
        clientMongoTemplate.enlever("nomEntreprise","entreprise1");
    }

    /**
     * Les tests avec clientEffectif true et false ne sont pas possible pour l'instant
     */
    @Test
    void recupererClientSpecifiqueTest() throws InterruptedException {
        clientMongoTemplate.enlever("nomEntreprise","entreprise113244");

        Client client = new Client();
        client.setNomEntreprise("entreprise113244");
        client.setClientEffectif(true);

        Client client2 = new Client();
        client2.setDescriptif("Descriptif");
        client2.setNomEntreprise("entreprise113244");
        client2.setClientEffectif(true);

        clientMongoTemplate.mongoTemplate.insert(List.of(client,client2),Client.class);

        List<Client> clients =  clientMongoTemplate.trouverPar("nomEntreprise","entreprise113244");
        Assertions.assertEquals( clients
                .size(), 2,"L'insertion n'a pas fonctionné. " + clients.size() + " a été inséré");
        Client donneesRecherche = new Client();

        donneesRecherche.setNomEntreprise("entreprise113244");
        donneesRecherche.setClientEffectif(true);


        List<Client> clientsTrouves = clientMongoTemplate.getEntitesDepuis(donneesRecherche);

        Assertions.assertEquals(clientsTrouves.size(), 2 , "Les deux clients n'ont pas été trouvés. Taille de la liste " + clientsTrouves.size() );
        clientMongoTemplate.enlever("nomEntreprise","entreprise113244");

    }


}
