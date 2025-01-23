package friutrodez.backendtourneecommercial.authentification;

import com.fasterxml.jackson.databind.ObjectMapper;
import friutrodez.backendtourneecommercial.dto.JwtToken;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("production")
public class AuthentificationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    JwtToken tokenUtilisateur1;
     String utilisateurJson;
     String utilisateurJson2;
    @BeforeAll
    void Setup() throws Exception {
        Utilisateur testUtilisateur = new Utilisateur();
        testUtilisateur.setNom("testuser");
        testUtilisateur.setPrenom("testPrenom");
        testUtilisateur.setMotDePasse("password");
        testUtilisateur.setEmail("Email1@email.com");

        Utilisateur testUtilisateur2 = new Utilisateur();
        testUtilisateur2.setNom("testUser2");
        testUtilisateur2.setPrenom("testPrenom2");
        testUtilisateur2.setMotDePasse("password");
        testUtilisateur2.setEmail("Email@mail.com");

        utilisateurJson = objectMapper.writeValueAsString(testUtilisateur);
        utilisateurJson2 = objectMapper.writeValueAsString(testUtilisateur2);

        mockMvc.perform(post("/auth/creer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utilisateurJson))

                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.notNullValue()));

        mockMvc.perform(post("/auth/creer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utilisateurJson2))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.notNullValue())).andDo(print());

        MvcResult mvcResult = mockMvc.perform(post("/auth/authentifier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utilisateurJson))
                .andExpect(status().isOk()).andDo(print()).andReturn();

        tokenUtilisateur1 =  objectMapper.readValue(mvcResult.getResponse().getContentAsString(), JwtToken.class);

    }

    @Test
    void testMemeToken2Authentification() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/auth/authentifier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utilisateurJson))
                .andExpect(status().isOk()).andReturn();

        JwtToken token =  objectMapper.readValue(mvcResult.getResponse().getContentAsString(), JwtToken.class);

        Assertions.assertNotEquals(token.token(),tokenUtilisateur1.token(),"Les tokens sont les mêmes pour deux authentifications");
    }

    @Test
    void testTokenDifferent2Utilisateurr() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/auth/authentifier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utilisateurJson2))
                .andExpect(status().isOk()).andReturn();

        JwtToken token =  objectMapper.readValue(mvcResult.getResponse().getContentAsString(), JwtToken.class);


        Assertions.assertNotEquals(token.token(),tokenUtilisateur1.token(),"Les tokens sont les mêmes pour deux authentifications");
    }

    @Test
    void testAuthentificationNonCree() throws Exception {
        Utilisateur utilisateurNonCree = new Utilisateur();
        utilisateurNonCree.setMotDePasse("password");
        utilisateurNonCree.setPrenom("nonCree");
        utilisateurNonCree.setNom("non");

         mockMvc.perform(post("/auth/authentifier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(utilisateurNonCree)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAccesSansToken() throws Exception {
        mockMvc.perform(get("/auth"))
                .andExpect(status().isForbidden());
    }


}
