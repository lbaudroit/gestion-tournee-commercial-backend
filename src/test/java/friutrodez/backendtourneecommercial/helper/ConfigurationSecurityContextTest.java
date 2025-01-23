package friutrodez.backendtourneecommercial.helper;

import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mysql.UtilisateurRepository;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Component;

import static org.mockito.Mockito.when;

/**
 * Classe utilitaire pour aider à la configuration du securityContext pour les tests
 */
@Component
public class ConfigurationSecurityContextTest {

    private Utilisateur toMock;
    @Autowired
    UtilisateurRepository utilisateurRepository;
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
