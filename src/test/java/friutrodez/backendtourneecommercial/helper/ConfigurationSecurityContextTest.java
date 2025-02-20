package friutrodez.backendtourneecommercial.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import friutrodez.backendtourneecommercial.dto.DonneesAuthentification;
import friutrodez.backendtourneecommercial.dto.JwtToken;
import friutrodez.backendtourneecommercial.model.*;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import friutrodez.backendtourneecommercial.repository.mysql.UtilisateurRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Classe utilitaire pour aider à la configuration des tests
 * notamment ceux liés à la securité et à la vérification.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
@Component
public class ConfigurationSecurityContextTest {

    private static final Adresse CORRECT_ADRESSE = new Adresse("11 Place du Portail- Haut", "12390", "Rignac");
    private static final Coordonnees CORRECT_COORDONNEES = new Coordonnees(44.409773, 2.288314);

    private static final Contact CORRECT_CONTACT = new Contact("nom", "test", "0102030405");
    private static final Client CORRECT_CLIENT = Client.builder().clientEffectif(true).nomEntreprise("TestEntreprise")
            .contact(CORRECT_CONTACT)
            .adresse(CORRECT_ADRESSE)
            .coordonnees(CORRECT_COORDONNEES).build();


    private final ObjectMapper objectMapper;
    private final ClientMongoTemplate clientMongoTemplate;
    private final UtilisateurRepository utilisateurRepository;

    private Utilisateur userFromToken;
    private JwtToken token;

    /**
     * @param objectMapper          Un json mapper pour convertir les données en json.
     * @param clientMongoTemplate   Template pour les clients.
     * @param utilisateurRepository Repository pour les utilisateurs.
     */
    public ConfigurationSecurityContextTest(ObjectMapper objectMapper, ClientMongoTemplate clientMongoTemplate, UtilisateurRepository utilisateurRepository) {
        this.objectMapper = objectMapper;
        this.clientMongoTemplate = clientMongoTemplate;
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Méthode pour récupérer un token qui sera utilisé pendant les tests.
     *
     * @param mockMvc Le mock mvc du test.
     * @return Le token.
     * @throws Exception en cas d'échec du mockMVC ou de la conversion en JSON
     */
    public String getTokenForSecurity(MockMvc mockMvc) throws Exception {
        if (userFromToken == null) {
            userFromToken = Utilisateur.builder()
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
            String userJson = objectMapper.writeValueAsString(userFromToken);

            mockMvc.perform(post("/utilisateur/").contentType(MediaType.APPLICATION_JSON)
                    .content(userJson)).andExpect(status().isOk()).andReturn();
            userFromToken = utilisateurRepository.findByNom("TestNomConfig");
        }
        if (token == null) {
            DonneesAuthentification authenticationData = new DonneesAuthentification(userFromToken.getEmail(), "Benjamin.123@d");
            String userJson = objectMapper.writeValueAsString(authenticationData);
            MvcResult result = mockMvc.perform(post("/auth/").contentType(MediaType.APPLICATION_JSON).content(userJson)).andExpect(status().isOk()).andReturn();
            token = objectMapper.readValue(result.getResponse().getContentAsString(), JwtToken.class);

        }
        return token.token();
    }

    /**
     * Méthode permettant de récupérer un utilisateur avec un email aléatoire afin d'être utilisé pendant les tests.
     *
     * @return Un utilisateur avec des données correctes.
     */
    public Utilisateur getMockUser() {
        Utilisateur user = Utilisateur.builder()
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
        utilisateurRepository.save(user);
        return user;
    }

    /**
     * Méthode permettant de récupérer un client avec un nom d'entreprise aléatoire.
     *
     * @param user L'utilisateur lié au client qui va être créé.
     * @return Un client avec des données correctes.
     */
    public Client getMockClient(Utilisateur user) {
        Client client = CORRECT_CLIENT;
        client.set_id(null);
        client.setNomEntreprise((Math.random() * 10000) + "Test");
        client.setIdUtilisateur(String.valueOf(user.getId()));
        clientMongoTemplate.save(client);

        return clientMongoTemplate.find("_id", client.get_id()).getFirst();
    }

    /**
     * Méthode pour générer aléatoirement un email avec Math.random.
     *
     * @return Un email avec des chiffres pseudo aléatoires.
     */
    private String getRandomEmail() {
        double firstPart = Math.random() * 10000;
        double domain = Math.random() * 10000;
        return firstPart + "@" + domain + ".fr";
    }

    /**
     * Méthode pour récupérer l'utilisateur actuel du token récupéré.
     *
     * @return L'utilisateur actuel du token récupéré.
     */
    public Utilisateur getUser() {
        return userFromToken;
    }

    /**
     * Méthode pour réinitialiser l'utilisateur à récupérer.
     */
    public void resetUser() {
        utilisateurRepository.delete(userFromToken);
        userFromToken = null;
        token = null;
    }

}
