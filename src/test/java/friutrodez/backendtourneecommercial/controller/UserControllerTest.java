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
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Classe de test pour UtilisateurController.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
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

    /**
     * Setup nécessaire pour faire fonctionner les tests.
     * @throws Exception si le token n'a pas été récupéré.
     */
    @BeforeAll
    void setSecurity() throws Exception {
        headerToken = configurationSecurityContextTest.getTokenForSecurity(mockMvc);
        testUser = configurationSecurityContextTest.getUser();
    }

    /**
     * Teste la modification d'un utilisateur à partir du controlleur.
     */
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
