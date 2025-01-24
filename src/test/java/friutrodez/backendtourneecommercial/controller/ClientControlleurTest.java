package friutrodez.backendtourneecommercial.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import friutrodez.backendtourneecommercial.helper.ConfigurationSecurityContextTest;
import friutrodez.backendtourneecommercial.model.Adresse;
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
@ActiveProfiles("production")
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

    String headerToken;
    @Autowired
    ConfigurationSecurityContextTest configurationSecurityContextTest;
    @BeforeEach
    void setupSecurityContext() {
        //configurationSecurityContextTest.setSecurityContext();
    }

    @BeforeAll
    void Setup() throws Exception {
        client = new Client();
        client.setNomEntreprise("entrepriseTest");
        Adresse adresse = new Adresse("6 Impasse du Suc","81490","Boissezon");
        client.setAdresse(adresse);
        headerToken = configurationSecurityContextTest.getTokenForSecurity(mockMvc);
        //configurationSecurityContextTest.setSecurityContext();
    }

    @Order(1)
    @Test
    void creationClientTest() throws Exception {
        String jsonClient= objectMapper.writeValueAsString(client);
        mockMvc.perform(put("/client/creer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonClient).header("Authorization", "Bearer " + headerToken))
                .andExpect(status().isOk());
        Client clientTrouve = clientMongoTemplate.findOne("nomEntreprise","entrepriseTest");
        Assertions.assertEquals(client,clientTrouve,"Le client n'a pas été modifié");
        Assertions.assertEquals(clientTrouve.getIdUtilisateur(),""+configurationSecurityContextTest.getUtilisateur().getId());
        client = clientTrouve;
    }

    @Order(4)
    @Test
    void clientAvecMauvaisesInformationsCreation() throws Exception {
        Client client = new Client();

        String jsonClient = objectMapper.writeValueAsString(client);

        mockMvc.perform(put("/client/creer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonClient).header("Authorization","Bearer " +headerToken ))
                .andExpect(status().isBadRequest());
        client.setNomEntreprise("entrepriseTest");

        jsonClient = objectMapper.writeValueAsString(client);
    }

    @Order(2)
    @Test
    void clientRecupererUnTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/client/recuperer/")
                .param("id",client.get_id()).header("Authorization","Bearer " +headerToken ))
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
                .content(jsonModifier).header("Authorization","Bearer " +headerToken ))

                .andExpect(status().isOk()).andReturn();


        Client clientRecupere = clientMongoTemplate.findOne("nomEntreprise","Test Modification");

        Assertions.assertEquals(clientModifier,clientRecupere,"Le client n'a pas été modifié");
        Assertions.assertNotEquals(clientRecupere.getNomEntreprise(),client.getNomEntreprise(),"Le client n'a pas été modifié");
        clientMongoTemplate.removeOne("nomEntreprise","Test Modification");
    }

    @Order(6)
    @Test
    void clientSupprimerTest() throws Exception {
        MvcResult mvcResult= mockMvc.perform(delete("/client/supprimer/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id",client.get_id()).header("Authorization","Bearer " +headerToken ))
                .andExpect(status().isOk()).andReturn();
        Assertions.assertNull(clientMongoTemplate.getOneClient(client.get_id(),String.valueOf( configurationSecurityContextTest.getUtilisateur().getId())));
    }

    @Order(5)
    @Test
    void clientRecupererTousTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/client/").header("Authorization","Bearer " +headerToken ))
                .andExpect(status().isOk()).andReturn();


    }

}
