package friutrodez.backendtourneecommercial.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import friutrodez.backendtourneecommercial.helper.ConfigurationSecurityContextTest;
import friutrodez.backendtourneecommercial.model.Client;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * PLAN DE TEST
 * <p>
 * Endpoint GET `/itineraire/generate` - nécessitant authentification
 * <p>
 * | ID | Cas               | Données attendues dans le body                 | Code HTTP attendu |
 * |  1 | Un client connu   | Nombre de km non null, identifiant du client   | 200 (OK)          |
 * |  2 | Un client inconnu | ∅                                              | 400 (Bad Request) |
 */
@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("production")
public class ItineraireControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    Client client;
    Client client2;

    String headerToken;
    @Autowired
    ConfigurationSecurityContextTest configurationSecurityContextTest;

    @BeforeAll
    void Setup() throws Exception {
        headerToken = configurationSecurityContextTest.getTokenForSecurity(mockMvc);
        client = configurationSecurityContextTest.getMockClient(configurationSecurityContextTest.getUser());
        client2 = configurationSecurityContextTest.getMockClient(configurationSecurityContextTest.getUser());
        client.setNomEntreprise("entrepriseTest");
    }

    @Order(1)
    @Test
    void testGenerateKnownClient() throws Exception {
        String validClientId = client.get_id();
        String clientAsJson = objectMapper.writeValueAsString(client);
        System.out.println(clientAsJson);
        mockMvc.perform(get("/itineraire/generate")
                        .param("clients", validClientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientAsJson).header("Authorization", "Bearer " + headerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kilometres").isNotEmpty())
                .andExpect(jsonPath("$.clients[0]._id").value(validClientId));
    }

    @Order(2)
    @Test
    void testGenerateUnknownClient() throws Exception {
        String invalidClientId = "99999999";

        String clientAsJson = objectMapper.writeValueAsString(client);
        System.out.println(clientAsJson);
        mockMvc.perform(get("/itineraire/generate")
                        .param("clients", invalidClientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientAsJson).header("Authorization", "Bearer " + headerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }
}
