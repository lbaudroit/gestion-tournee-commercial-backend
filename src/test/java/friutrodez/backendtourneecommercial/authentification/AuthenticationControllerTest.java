package friutrodez.backendtourneecommercial.authentification;

import com.fasterxml.jackson.databind.ObjectMapper;
import friutrodez.backendtourneecommercial.controller.AuthenticationController;
import friutrodez.backendtourneecommercial.dto.DonneesAuthentification;
import friutrodez.backendtourneecommercial.dto.JwtToken;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.service.UtilisateurService;
import friutrodez.backendtourneecommercial.service.JwtService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UtilisateurService authenticationService;
    @MockitoBean
    private JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationController authenticationController;

    @Transactional
    @Rollback
    @Test
    void testUtilisateurCreationDeCompte() throws Exception {
        Utilisateur testUser = new Utilisateur();
        testUser.setNom("testuser");
        testUser.setPrenom("testPrenom");
        testUser.setMotDePasse("pAss@dz2");
        testUser.setEmail("Email@mail2.com");
        testUser.setLibelleAdresse("50 Avenue de Bordeaux");

        testUser.setCodePostal("12000");
        testUser.setVille("Rodez");

        String userAsJson = objectMapper.writeValueAsString(testUser);

        mockMvc.perform(post("/utilisateur/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userAsJson))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.notNullValue()));

    }

    @Test
    void testToken() throws Exception {
        Utilisateur testUser = new Utilisateur();
        testUser.setNom("testuser");
        testUser.setPrenom("testPrenom");
        testUser.setEmail("Email@mail2.com");
        testUser.setMotDePasse("pA3@.AZet4");
        testUser.setLibelleAdresse("50 Avenue de Bordeaux");

        testUser.setCodePostal("12000");
        testUser.setVille("Rodez");
        String userAsJson = objectMapper.writeValueAsString(testUser);

        String expectedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";

        JwtToken jwtToken = new JwtToken(expectedToken, 1800000);

        UserDetails userDetailsMock = mock(UserDetails.class);
        when(userDetailsMock.getUsername()).thenReturn("Email@mail2.com");
        when(userDetailsMock.getPassword()).thenReturn("password");

        when(jwtService.generateToken(any(UserDetails.class)))
                .thenReturn(expectedToken);

        mockMvc.perform(post("/utilisateur/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userAsJson))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.notNullValue()));

        DonneesAuthentification authenticationData = new DonneesAuthentification("Email@mail2.com", "pA3@.AZet4");

        mockMvc.perform(post("/auth/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(expectedToken))
                .andExpect(content().string(org.hamcrest.Matchers.notNullValue())).andReturn();

        when(jwtService.extractEmail(any(String.class))).thenReturn("Email@mail2.com");
        when(jwtService.isTokenValid(any(String.class), any(UserDetails.class))).thenReturn(true);

        // appel à une méthode nécessitant authentification
        mockMvc.perform(get("/utilisateur/")
                        .header("Authorization", "Bearer " + expectedToken))
                .andExpect(status().isOk());

    }


}
