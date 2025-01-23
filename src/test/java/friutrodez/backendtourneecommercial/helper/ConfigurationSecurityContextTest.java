package friutrodez.backendtourneecommercial.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import friutrodez.backendtourneecommercial.dto.JwtToken;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mysql.UtilisateurRepository;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Classe utilitaire pour aider à la configuration du securityContext pour les tests
 */
@Component
public class ConfigurationSecurityContextTest {

    @Autowired
    ObjectMapper objectMapper;
    private Utilisateur toMock;
    @Autowired
    UtilisateurRepository utilisateurRepository;

    JwtToken token;
    /**
     * Configure le securityContext avec un mock et un user récupéré dans la bd.
     * A utiliser dans le beforeEach d'un test pour fonctionner correctement.
     */
    public void setSecurityContext() {
        if(toMock==null) {
            toMock = utilisateurRepository.findByNom("Nicol");

        }
        setUpSecurityContext(toMock);
    }

    public String getTokenForSecurity(MockMvc mockMvc) throws Exception {
        if(toMock==null) {
            toMock = utilisateurRepository.findByNom("Nicol");
        }
        if(token == null) {
            String utilisateurJson = objectMapper.writeValueAsString(toMock);

            //FIXME Une erreur est envoyée disant que le mot de passe entre l'utilisateur est celui dans la bd n'est pas le meme
            MvcResult resultat= mockMvc.perform(post("/auth/authentifier").contentType(MediaType.APPLICATION_JSON).content(utilisateurJson)).andExpect(status().isOk()).andReturn();
            token = objectMapper.readValue(resultat.getResponse().getContentAsString(), JwtToken.class);

        }
        return token.token();
    }
    public void setSecurityContextAvecUtilisateur(Utilisateur utilisateur) {
        //toMock = utilisateur;

        toMock = utilisateurRepository.findByNom(utilisateur.getNom());
        if(toMock == null) {
            System.out.println("Erruers");
        }
        setUpSecurityContext(toMock);
    }


    private void setUpSecurityContext(Utilisateur utilisateur) {
        SecurityContextHolder.clearContext();
        
        SecurityContext mockSecurityContext = Mockito.mock(SecurityContext.class);
        Authentication mockAuthentication = Mockito.mock(Authentication.class);

        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);

        when(mockAuthentication.getPrincipal()).thenReturn(utilisateur);

        //when(mockHolder.getContext()).thenReturn(mockSecurityContext);
        SecurityContextHolder.setContext(mockSecurityContext);
    }

    public Utilisateur getUtilisateur() {
        return  toMock;
    }

}
