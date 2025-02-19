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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Classe de test pour tester 'l'authentification'.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("production")
public class AuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    JwtToken userToken;
    String userAsJson1;
    String userAsJson2;

    /**
     * Setup nécessaire pour faire fonctionner les tests.
     */
    @BeforeAll
    void Setup() throws Exception {
        Utilisateur testUser = new Utilisateur();
        testUser.setNom("testuser");
        testUser.setPrenom("testPrenom");
        testUser.setMotDePasse("A232Eez@d");
        testUser.setEmail("Email1@email.com");
        testUser.setLibelleAdresse("50 Avenue de Bordeaux");

        testUser.setCodePostal("12000");
        testUser.setVille("Rodez");

        Utilisateur testUser2 = new Utilisateur();
        testUser2.setNom("testUser2");
        testUser2.setPrenom("testPrenom2");
        testUser2.setMotDePasse("A232Eez@d ");
        testUser2.setEmail("Email2@mail.com");
        testUser2.setLibelleAdresse("50 Avenue de Bordeaux");

        testUser2.setCodePostal("12000");
        testUser2.setVille("Rodez");

        userAsJson1 = objectMapper.writeValueAsString(testUser);
        userAsJson2 = objectMapper.writeValueAsString(testUser2);

        mockMvc.perform(post("/utilisateur/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userAsJson1))

                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.notNullValue()));

        mockMvc.perform(post("/utilisateur/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userAsJson2))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.notNullValue())).andDo(print());

        MvcResult mvcResult = mockMvc.perform(post("/auth/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userAsJson1))
                .andExpect(status().isOk()).andDo(print()).andReturn();

        userToken = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), JwtToken.class);

    }

    /**
     * Teste que les tokens sont différents pour deux authentifications d'un seul utilisateur.
     */
    @Test
    void testTokenShouldBeDifferentForTwoAuthentication() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/auth/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userAsJson1))
                .andExpect(status().isOk()).andReturn();

        JwtToken token = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), JwtToken.class);

        Assertions.assertNotEquals(token.token(), userToken.token(), "Les tokens sont les mêmes pour deux authentifications");
    }

    /**
     * Teste que les tokens entre deux utilisateurs sont différents lors de l'authentification.
     */
    @Test
    void testTokenShouldBeDifferentForTwoDistinctUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/auth/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userAsJson2))
                .andExpect(status().isOk()).andReturn();

        JwtToken token = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), JwtToken.class);


        Assertions.assertNotEquals(token.token(), userToken.token(), "Les tokens sont les mêmes pour deux authentifications");
    }

    /**
     * Teste que l'authentification n'est pas faite si l'utilisateur n'existe pas dans la bd.
     */
    @Test
    void testAuthenticationForbidden() throws Exception {
        Utilisateur userToAuthenticate = new Utilisateur();
        userToAuthenticate.setMotDePasse("password");
        userToAuthenticate.setPrenom("nonCree");
        userToAuthenticate.setNom("non");

        mockMvc.perform(post("/auth/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToAuthenticate)))
                .andExpect(status().isForbidden());
    }

    /**
     * Teste que l'accés n'est pas possible si aucun token n'est donné à une ressource dont un token est nécessaire.
     */
    @Test
    void testNoTokenAccess() throws Exception {
        mockMvc.perform(get("/utilisateur/"))
                .andExpect(status().isForbidden());
    }


}
