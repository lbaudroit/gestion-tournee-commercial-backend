package friutrodez.backendtourneecommercial.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Coordonnees;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
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
    void Setup() throws Exception {
        client = new Client();
        client.setNomEntreprise("entrepriseTest");
        client.setIdUtilisateur("1");
        client.setCoordonnees(new Coordonnees(20, 20));

    }

    @Order(1)
    @Test
    void creationClientTest() throws Exception {
        String jsonClient= objectMapper.writeValueAsString(client);
        mockMvc.perform(put("/client/creer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonClient))
                .andExpect(status().isOk());
        Client clientTrouve = clientMongoTemplate.findOne("nomEntreprise","entrepriseTest");
        Assertions.assertEquals(client,clientTrouve,"Le client n'a pas été modifié");
        client.set_id(clientTrouve.get_id());
    }

    @Order(4)
    @Test
    void clientSansNomEtCoordonnee() throws Exception {
        Client client = new Client();
        client.setIdUtilisateur("1");

        String jsonClient = objectMapper.writeValueAsString(client);
//        mockMvc.perform(put("/client/creer")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonClient))
//                .andExpect(status().isBadRequest());

        client.setNomEntreprise("entrepriseTest");

        jsonClient = objectMapper.writeValueAsString(client);
//        mockMvc.perform(put("/client/creer")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonClient))
//                .andExpect(status().isBadRequest());

        client.setCoordonnees(new Coordonnees(20, 20));

        jsonClient = objectMapper.writeValueAsString(client);
        mockMvc.perform(put("/client/creer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonClient))
                .andExpect(status().isOk());


        clientMongoTemplate.removeOne("nomEntreprise","entrepriseTest");
    }

    @Order(2)
    @Test
    void clientRecupererUnTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/client/recuperer/")
                .param("id",client.get_id()))
                .andExpect(status().isOk()).andReturn();

        Assertions.assertDoesNotThrow(()->objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Client.class),"Aucune donnée a été récupéré");
        Client clientRecupere = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Client.class);

        Assertions.assertEquals(clientRecupere,client);
    }

    @Order(3)
    @Test
    void clientModifierTest() throws Exception {
        clientMongoTemplate.save(client);
        if(client.get_id() == null || client.get_id().isEmpty()) {
            throw new RuntimeException("Le client n'a pas d'id");
        }
        Client clientModifier = new Client();
        clientModifier.setCoordonnees(new Coordonnees(0,0));
        clientModifier.setNomEntreprise("Test Modification");
        String jsonModifier = objectMapper.writeValueAsString(clientModifier);

        MvcResult mvcResult= mockMvc.perform(post("/client/modifier/")
                        .param("id",client.get_id())

                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonModifier))
                .andExpect(status().isOk()).andReturn();


        Client clientRecupere = clientMongoTemplate.findOne("nomEntreprise","Test Modification");

        Assertions.assertEquals(clientModifier,clientRecupere,"Le client n'a pas été modifié");
        Assertions.assertNotEquals(clientRecupere.getNomEntreprise(),client.getNomEntreprise(),"Le client n'a pas été modifié");
        clientMongoTemplate.removeOne("nomEntreprise","Test Modification");
    }


    @Order(5)
    @Test
    void clientSupprimerTest() throws Exception {
        clientMongoTemplate.save(client);
        if(client.get_id() == null || client.get_id().isEmpty()) {
            throw new RuntimeException("Le client n'a pas d'id");
        }

        String clientSupprimer = objectMapper.writeValueAsString(client);
        MvcResult mvcResult= mockMvc.perform(delete("/client/supprimer/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientSupprimer))
                .andExpect(status().isOk()).andReturn();
    }

}
