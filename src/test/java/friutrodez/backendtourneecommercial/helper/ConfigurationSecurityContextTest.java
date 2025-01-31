package friutrodez.backendtourneecommercial.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import friutrodez.backendtourneecommercial.dto.DonneesAuthentification;
import friutrodez.backendtourneecommercial.dto.JwtToken;
import friutrodez.backendtourneecommercial.model.Adresse;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Contact;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
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

    private static final Adresse CORRECT_ADRESSE = new Adresse("11 Pl. du Portail Haut", "12390", "Rignac");

    private static final Contact CORRECT_CONTACT = new Contact("nom","test","0102030405");
    private static final Client CORRECT_CLIENT = Client.builder().clientEffectif(true).nomEntreprise("TestEntreprise")
            .contact(CORRECT_CONTACT)
            .adresse(CORRECT_ADRESSE).build();

    @Autowired
    ObjectMapper objectMapper;
    private Utilisateur toMock;

    @Autowired
    ClientMongoTemplate clientMongoTemplate;
    @Autowired
    UtilisateurRepository utilisateurRepository;

    JwtToken token;



    public String getTokenForSecurity(MockMvc mockMvc) throws Exception {
        if(toMock==null) {
            Utilisateur utilisateur4 = Utilisateur.builder()
                    .nom("TestNomConfig")
                    .prenom("TestPrenom")
                    .email("te@no.fr")
                    .motDePasse("Benjamin.123@d")
                    .libelleAdresse("6 Impasse du Suc")
                    .codePostal("81490")
                    .ville("Boissezon")
                    .latitude(43.5775202)
                    .longitude(2.3694482)
                    .build();
            toMock = utilisateur4;
            String utilisateurJson = objectMapper.writeValueAsString(toMock);

             mockMvc.perform(post("/auth/creer").contentType(MediaType.APPLICATION_JSON).content(utilisateurJson)).andExpect(status().isOk()).andReturn();
             toMock = utilisateurRepository.findByNom("TestNomConfig");
        }
        if(token == null) {
            DonneesAuthentification donneesAuthentification = new DonneesAuthentification(toMock.getEmail(),"Benjamin.123@d");
            String utilisateurJson = objectMapper.writeValueAsString(donneesAuthentification);
            //FIXME Une erreur est envoyée disant que le mot de passe entre l'utilisateur est celui dans la bd n'est pas le meme
            MvcResult resultat= mockMvc.perform(post("/auth/authentifier").contentType(MediaType.APPLICATION_JSON).content(utilisateurJson)).andExpect(status().isOk()).andReturn();
            token = objectMapper.readValue(resultat.getResponse().getContentAsString(), JwtToken.class);

        }
        return token.token();
    }
    public Utilisateur getMockUser() {
        Utilisateur utilisateur = Utilisateur.builder()
                .nom("TestRandom")
                .prenom("TestRandom")
                .email(getRandomEmail())
                .motDePasse("Benjamin.123@d")
                .libelleAdresse("6 Impasse du Suc")
                .codePostal("81490")
                .ville("Boissezon")
                .latitude(43.5775202)
                .longitude(2.3694482)
                .build();
        utilisateurRepository.save(utilisateur);
        return utilisateur;
    }

    public Client getMockClient(Utilisateur user) {
        Client client = CORRECT_CLIENT;
        client.set_id(null);
        client.setNomEntreprise((Math.random() * 10000)+"Test");
        client.setIdUtilisateur(String.valueOf(user.getId()));
        clientMongoTemplate.save(client);
        Client clientFound = clientMongoTemplate.find("_id" ,client.get_id()).getFirst();

        return clientFound;
    }
    private String getRandomEmail() {
        double firstPart = Math.random() * 10000;
        double domain = Math.random() * 10000;
        return firstPart+"@"+domain+".fr";
    }

    public Utilisateur getUtilisateur() {
        return  toMock;
    }

}
