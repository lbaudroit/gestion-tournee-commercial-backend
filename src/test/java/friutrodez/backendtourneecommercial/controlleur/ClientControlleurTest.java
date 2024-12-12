package friutrodez.backendtourneecommercial.controlleur;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import friutrodez.backendtourneecommercial.controller.ClientControlleur;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Coordonnees;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientControlleurTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ClientControlleur clientControlleur;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ClientMongoTemplate clientMongoTemplate;

    Client client;
    @BeforeAll
    void Setup() {
        client = new Client();
        client.setNomEntreprise("entrepriseTest");
        client.setIdUtilisateur("1");
        client.setCoordonnees(new Coordonnees(20,20));
    }
    @Test
    void creationClientTest() throws Exception {
         String jsonClient= objectMapper.writeValueAsString(client);
        mockMvc.perform(put("/client/creer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonClient))
                .andExpect(status().isOk());
        Client clientTrouve = clientMongoTemplate.trouverUn("nomEntreprise","entrepriseTest");
        Assertions.assertEquals(client,clientTrouve);
    }

    @Test
    void clientSansNomEtCoordonnee() throws Exception {
        Client client = new Client();
        client.setIdUtilisateur("1");

        String jsonClient= objectMapper.writeValueAsString(client);
        mockMvc.perform(put("/client/creer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonClient))
                .andExpect(status().isBadRequest());

        client.setNomEntreprise("entrepriseTest");

        jsonClient= objectMapper.writeValueAsString(client);
        mockMvc.perform(put("/client/creer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonClient))
                .andExpect(status().isBadRequest());

        client.setCoordonnees(new Coordonnees(20,20));

        jsonClient= objectMapper.writeValueAsString(client);
        mockMvc.perform(put("/client/creer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonClient))
                .andExpect(status().isOk());

        clientMongoTemplate.enlever("nomEntreprise","entrepriseTest");

        

    }

    @Test
    void clientRecupererUnTest() throws Exception {

        client.set_id("5");
        String jsonClient= objectMapper.writeValueAsString(client);

        // FIXME la donnée n'est pas récupéré
        MvcResult mvcResult = mockMvc.perform(get("/client/recuperer/?id=5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonClient)).andExpect(status().isOk()).andReturn();

        Assertions.assertDoesNotThrow(()->objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Client.class),"Aucune donnée a été récupéré");
        Client clientRecupere = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Client.class);

        Assertions.assertEquals(clientRecupere,client);
    }

    @AfterAll
    void supprimerlesDonnees() {
        clientMongoTemplate.enlever("nomEntreprise","entrepriseTest");
    }

}
