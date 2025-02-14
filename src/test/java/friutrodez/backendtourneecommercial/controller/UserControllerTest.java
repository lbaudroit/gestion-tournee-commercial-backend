package friutrodez.backendtourneecommercial.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import friutrodez.backendtourneecommercial.helper.ConfigurationSecurityContextTest;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    ConfigurationSecurityContextTest configurationSecurityContextTest;

    Utilisateur testUser;

    String headerToken;

    @BeforeAll
    void setSecurity() throws Exception {
        headerToken = configurationSecurityContextTest.getTokenForSecurity(mockMvc);
        testUser = configurationSecurityContextTest.getUser();
    }

    @Test
    void testEditUser() throws Exception {
        testUser.setNom("modificationTestUser");

        String utilisateurJson = objectMapper.writeValueAsString(testUser);

        mockMvc.perform(put("/utilisateur/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utilisateurJson)
                        .header("Authorization", "Bearer " + headerToken))
                .andExpect(status().isOk());
    }
}
