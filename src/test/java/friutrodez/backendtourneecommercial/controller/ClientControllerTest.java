package friutrodez.backendtourneecommercial.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import friutrodez.backendtourneecommercial.helper.ConfigurationSecurityContextTest;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("production")
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ClientMongoTemplate clientMongoTemplate;

    Client client;

    String headerToken;
    @Autowired
    ConfigurationSecurityContextTest configurationSecurityContextTest;

    @BeforeAll
    void Setup() throws Exception {
        headerToken = configurationSecurityContextTest.getTokenForSecurity(mockMvc);
        client = configurationSecurityContextTest.getMockClient(configurationSecurityContextTest.getUser());
        client.setNomEntreprise("entrepriseTest");
    }

    @Order(1)
    @Test
    void testCreationClient() throws Exception {
        String clientAsJson = objectMapper.writeValueAsString(client);
        System.out.println(clientAsJson);
        mockMvc.perform(post("/client/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientAsJson).header("Authorization", "Bearer " + headerToken))
                .andExpect(status().isOk());
        Client clientFound = clientMongoTemplate.findOne("nomEntreprise", "entrepriseTest");
        Assertions.assertEquals(client, clientFound, "Le client n'a pas été créé");
        Assertions.assertEquals(clientFound.getIdUtilisateur(), "" + configurationSecurityContextTest.getUser().getId());
        client = clientFound;
    }

    @Order(4)
    @Test
    void testClientWithWrongData() throws Exception {
        Client client = new Client();

        String clientAsJson = objectMapper.writeValueAsString(client);

        mockMvc.perform(post("/client/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientAsJson).header("Authorization", "Bearer " + headerToken))
                .andExpect(status().isBadRequest());
        client.setNomEntreprise("entrepriseTest");
    }

    @Order(2)
    @Test
    void testGetClient() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/client/" + client.get_id())
                        .header("Authorization", "Bearer " + headerToken))
                .andExpect(status().isOk()).andReturn();

        Assertions.assertDoesNotThrow(() -> objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Client.class), "Aucune donnée a été récupéré");
        Client clientRecupere = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Client.class);

        Assertions.assertEquals(clientRecupere, client);

    }

    @Order(3)
    @Test
    void testModifyClient() throws Exception {
        Client clientModifier = clientMongoTemplate.findOne("_id", client.get_id());
        clientModifier.setNomEntreprise("Test Modification");
        String jsonModifier = objectMapper.writeValueAsString(clientModifier);

        mockMvc.perform(put("/client/" + client.get_id())

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonModifier).header("Authorization", "Bearer " + headerToken))

                .andExpect(status().isOk());

        Client clientRecupere = clientMongoTemplate.findOne("nomEntreprise", "Test Modification");

        Assertions.assertEquals(clientModifier, clientRecupere, "Le client n'a pas été modifié");
        Assertions.assertNotEquals(clientRecupere.getNomEntreprise(), client.getNomEntreprise(), "Le client n'a pas été modifié");
    }

    @Order(6)
    @Test
    void testDeleteClient() throws Exception {
        mockMvc.perform(delete("/client/" + client.get_id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + headerToken))
                .andExpect(status().isOk());

        Optional<Client> client = clientMongoTemplate.getOneClient(this.client.get_id(), String.valueOf(configurationSecurityContextTest.getUser().getId()));
        Assertions.assertTrue(client.isEmpty());
    }

    @Order(5)
    @Test
    void testGetAllClients() throws Exception {
        client = configurationSecurityContextTest.getMockClient(configurationSecurityContextTest.getUser());
        client = configurationSecurityContextTest.getMockClient(configurationSecurityContextTest.getUser());

        MvcResult mvcResult = mockMvc.perform(
                get("/client/")
                        .header("Authorization", "Bearer " + headerToken))
                .andExpect(status().isOk()).andReturn();
        Assertions.assertEquals(3, mvcResult.getResponse().getContentAsString().split("},\\{").length);
    }

}
