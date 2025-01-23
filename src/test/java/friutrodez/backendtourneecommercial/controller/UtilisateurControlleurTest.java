package friutrodez.backendtourneecommercial.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import friutrodez.backendtourneecommercial.helper.ConfigurationSecurityContextTest;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UtilisateurControlleurTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    ConfigurationSecurityContextTest configurationSecurityContextTest;

    Utilisateur testUser;

    @BeforeEach
    void setSecurity() {
        configurationSecurityContextTest.setSecurityContext();
        testUser = configurationSecurityContextTest.getUtilisateur();
        Assertions.assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        Assertions.assertEquals("Nicol", ((Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNom());
    }

    @Test
    void modificationUtilisateurEtSuppresionTest()  throws Exception{
        testUser.setNom("modificationTestUser");

        String utilisateurJson = objectMapper.writeValueAsString(testUser);

         MvcResult mvcResultat = mockMvc.perform(post("/utilisateur/modifier")
                .contentType(MediaType.APPLICATION_JSON)
                .content(utilisateurJson))
                .andExpect(status().isOk()).andReturn();

       Utilisateur utilisateurModifie = objectMapper.readValue(mvcResultat.getResponse().getContentAsString(), Utilisateur.class);

        Assertions.assertEquals("modificationTestUser",utilisateurModifie.getNom());

        mockMvc.perform(delete("/utilisateur/supprimer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id",""+utilisateurModifie.getId()))
                .andExpect(status().isOk());


    }
}
