package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.dto.ItineraireCreationDTO;
import friutrodez.backendtourneecommercial.dto.ResultatOptimisation;
import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.helper.ConfigurationSecurityContextTest;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Coordonnees;
import friutrodez.backendtourneecommercial.model.Itineraire;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mysql.ItineraireRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
@Rollback
public class ItineraireServiceTest {

    @Autowired
    private ItineraireService itineraireService;

    @Autowired
    private ItineraireRepository itineraireRepository;

    private Utilisateur user;

    private static final String CORRECT_NAME = "Test";

    private static final String INCORRECT_NAME = "   ";

    private static final int CORRECT_DISTANCE = 10;

    private static final int INCORRECT_DISTANCE = -10;

    private static final String[] CORRECT_IDS = new String[]{"1", "2", "3", "4"};

    @Autowired
    private ConfigurationSecurityContextTest configurationSecurityContextTest;

    private Client clientFromOtherUser;

    /*
       plan de test:
            Itineraire avec client qui n'appartient pas au client       => throw error
            Itineraire avec plus de 8 client                            => throw error
            Itineraire avec pas de nom                                  => throw error
            Itineraire avec distance négative                           => throw error
            Itineraire avec client qui appartient au client et existe   => valide
    */

    @BeforeAll
    public void setUp() {

        user = configurationSecurityContextTest.getMockUser();
        Utilisateur user2 = configurationSecurityContextTest.getMockUser();

        Client client = configurationSecurityContextTest.getMockClient(user2);
        Client client2 = configurationSecurityContextTest.getMockClient(user2);
        Client client3 = configurationSecurityContextTest.getMockClient(user2);

        ItineraireCreationDTO dto = new ItineraireCreationDTO(INCORRECT_NAME,
                new String[]{client.get_id(), client2.get_id(), client3.get_id()},
                CORRECT_DISTANCE);


        Itineraire itineraire = setItineraireFromDTOAndUser(dto, user2);
        clientFromOtherUser = client;

    }

    @Test
    public void itineraireWithoutNameTest() {
        ItineraireCreationDTO dto = new ItineraireCreationDTO(INCORRECT_NAME, new String[]{"1"}, CORRECT_DISTANCE);
        Itineraire itineraire = setItineraireFromDTOAndUser(dto, user);
        Assertions.assertThrows(DonneesInvalidesException.class, () -> itineraireService.check(itineraire, user, dto));
    }

    @Test
    public void itineraireWithoutPositiveDistanceTest() {
        ItineraireCreationDTO dto = new ItineraireCreationDTO(CORRECT_NAME, new String[]{"1"}, INCORRECT_DISTANCE);
        Itineraire itineraire = setItineraireFromDTOAndUser(dto, user);
        Assertions.assertThrows(DonneesInvalidesException.class, () -> itineraireService.check(itineraire, user, dto));
    }

    @Test
    public void itineraireWithMoreThan8Clients() {
        String[] idClients = new String[9];
        for (int i = 0; i < 9; i++) {
            Client c = configurationSecurityContextTest.getMockClient(user);
            idClients[i] = c.get_id();
        }
        ItineraireCreationDTO dto = new ItineraireCreationDTO(CORRECT_NAME, idClients, CORRECT_DISTANCE);
        Itineraire itineraire = setItineraireFromDTOAndUser(dto, user);
        Assertions.assertThrows(DonneesInvalidesException.class, () -> itineraireService.check(itineraire, user, dto));

    }
    @Test
    void itineraireWithIncorrectClients() {
        ItineraireCreationDTO dto = new ItineraireCreationDTO(CORRECT_NAME, new String[]{null}, CORRECT_DISTANCE);
        Itineraire itineraire = setItineraireFromDTOAndUser(dto, user);
        Assertions.assertThrows(DonneesInvalidesException.class, () -> itineraireService.check(itineraire, user, dto));

        ItineraireCreationDTO dto2 = new ItineraireCreationDTO(CORRECT_NAME, new String[]{"-1","-5",String.valueOf(Integer.MAX_VALUE)}, CORRECT_DISTANCE);
        Itineraire itineraire2 = setItineraireFromDTOAndUser(dto2, user);
        Assertions.assertThrows(DonneesInvalidesException.class, () -> itineraireService.check(itineraire2, user, dto2));

    }

    @Test
    public void itineraireWithClientFromOtherClient() {
        ItineraireCreationDTO dto = new ItineraireCreationDTO(CORRECT_NAME, new String[]{clientFromOtherUser.get_id()}, CORRECT_DISTANCE);
        Itineraire itineraire = setItineraireFromDTOAndUser(dto, user);
        Assertions.assertThrows(DonneesInvalidesException.class, () -> itineraireService.check(itineraire, user, dto));
    }

    @Test
    public void correctItineraireTest() {
        String[] idClients = new String[8];
        for (int i = 0; i < 8; i++) {
            Client c = configurationSecurityContextTest.getMockClient(user);
            idClients[i] = c.get_id();
        }
        ItineraireCreationDTO dto = new ItineraireCreationDTO(CORRECT_NAME, idClients, CORRECT_DISTANCE);
        Itineraire itinerary = setItineraireFromDTOAndUser(dto, user);
        final  Itineraire[] savedItinerary = new Itineraire[1];
        Assertions.assertDoesNotThrow(() -> itineraireService.check(itinerary, user, dto));
        Assertions.assertDoesNotThrow(() -> savedItinerary[0] = itineraireService.createItineraire(dto, user));
        Assertions.assertTrue(itineraireRepository.existsById(savedItinerary[0].getId()));
        itineraireService.deleteItineraire(savedItinerary[0].getId(),user);
        Assertions.assertFalse(itineraireRepository.existsById(savedItinerary[0].getId()));
    }

    private Itineraire setItineraireFromDTOAndUser(ItineraireCreationDTO dto, Utilisateur user) {
        return Itineraire.builder().nom(dto.nom()).distance(dto.distance()).utilisateur(user).build();
    }

    @Test
    public void generateOptimizedPathTest() {
        Client[] clients = new Client[8];
        for (int i = 0; i < 8; i++) {
            Client c = configurationSecurityContextTest.getMockClient(user);
            // STUB Mettre en place de vrai coordonnées et vérifié avec le meilleur résultat
            c.setCoordonnees(new Coordonnees(0,0));
            clients[i] = c;
        }

        ResultatOptimisation resultatOptimisation = itineraireService.optimizeShortest(List.of(clients),user);
        Assertions.assertTrue(resultatOptimisation.clients().stream().allMatch(clientId -> doClientsContainsId(List.of(clients), clientId._id())));
        Assertions.assertTrue(resultatOptimisation.kilometres() >= 0);
        // TODO ajouter des vérifications plus intéressantes
    }

    private boolean doClientsContainsId(List<Client> clientList,String id) {
        boolean contains =false;
        for(Client client : clientList) {
            if(client.get_id().equals(id)) {
                contains = true;
                break;
            }
        }
        return contains;
    }
}
