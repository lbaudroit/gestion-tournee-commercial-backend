package friutrodez.backendtourneecommercial.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
@ActiveProfiles("test")

public class UtilisateurControlleurTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void modificationUtilisateurEtSuppresionTest()  throws Exception{
        Utilisateur testUser = new Utilisateur();
        testUser.setNom("testuser");
        testUser.setPrenom("testPrenom");
        testUser.setMotDePasse("password");

        String utilisateurJson = objectMapper.writeValueAsString(testUser);

        mockMvc.perform(post("/auth/creer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utilisateurJson))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.notNullValue()));

        testUser.setNom("modificationTestUser");

        utilisateurJson = objectMapper.writeValueAsString(testUser);

        MvcResult mvcResultat = mockMvc.perform(post("/utilisateur/modifier")
                .contentType(MediaType.APPLICATION_JSON)
                .content(utilisateurJson))
                .andExpect(status().isOk()).andReturn();
        

       Utilisateur utilisateurModifie = objectMapper.readValue(mvcResultat.getResponse().getContentAsString(), Utilisateur.class);

        Assertions.assertEquals("modificationTestUser",utilisateurModifie.getNom());
        Assertions.assertEquals("testPrenom",utilisateurModifie.getPrenom());

        mockMvc.perform(post("/utilisateur/supprimer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id",""+utilisateurModifie.getId()))
                .andExpect(status().isOk());


    }
}
