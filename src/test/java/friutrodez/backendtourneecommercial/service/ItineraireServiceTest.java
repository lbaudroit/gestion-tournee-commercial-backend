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

import java.util.List;

/**
 * Classe de test pour ItineraireServiceTest.
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
@Rollback
public class ItineraireServiceTest {

    @Autowired
    private ItineraireService itineraryService;

    @Autowired
    private ItineraireRepository itineraryRepository;

    private Utilisateur user;

    private static final String CORRECT_NAME = "Test";

    private static final String INCORRECT_NAME = "   ";

    private static final int CORRECT_DISTANCE = 10;

    private static final int INCORRECT_DISTANCE = -10;

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

    /**
     * Setup le contexte nécessaire pour le test.
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


        setItineraryFromDTOAndUser(dto, user2);
        clientFromOtherUser = client;

    }

    /**
     * Teste la création d'un itinéaraire avec un nom incorrect.
     */
    @Test
    public void itineraireWithoutNameTest() {
        ItineraireCreationDTO dto = new ItineraireCreationDTO(INCORRECT_NAME, new String[]{"1"}, CORRECT_DISTANCE);
        Itineraire itineraire = setItineraryFromDTOAndUser(dto, user);
        Assertions.assertThrows(DonneesInvalidesException.class, () -> itineraryService.check(itineraire, user, dto));
    }

    /**
     * Teste la création d'un itinéraire avec une distance négative.
     */
    @Test
    public void itineraryWithoutPositiveDistanceTest() {
        ItineraireCreationDTO dto = new ItineraireCreationDTO(CORRECT_NAME, new String[]{"1"}, INCORRECT_DISTANCE);
        Itineraire itineraire = setItineraryFromDTOAndUser(dto, user);
        Assertions.assertThrows(DonneesInvalidesException.class, () -> itineraryService.check(itineraire, user, dto));
    }

    /**
     * Teste la création d'un itinéraire avec plus de 8 clients.
     */
    @Test
    public void itineraireWithMoreThan8Clients() {
        final int CLIENTS_COUNT = Itineraire.MAX_CLIENTS + 1;
        String[] idClients = new String[CLIENTS_COUNT];
        for (int i = 0; i < CLIENTS_COUNT; i++) {
            Client c = configurationSecurityContextTest.getMockClient(user);
            idClients[i] = c.get_id();
        }
        ItineraireCreationDTO dto = new ItineraireCreationDTO(CORRECT_NAME, idClients, CORRECT_DISTANCE);
        Itineraire itineraire = setItineraryFromDTOAndUser(dto, user);
        Assertions.assertThrows(DonneesInvalidesException.class, () -> itineraryService.check(itineraire, user, dto));

    }

    /**
     * Teste la création d'un itinéraire avec un mauvais id client.
     */
    @Test
    void itineraireWithIncorrectClients() {
        ItineraireCreationDTO dto = new ItineraireCreationDTO(CORRECT_NAME, new String[]{null}, CORRECT_DISTANCE);
        Itineraire itineraire = setItineraryFromDTOAndUser(dto, user);
        Assertions.assertThrows(DonneesInvalidesException.class, () -> itineraryService.check(itineraire, user, dto));

        ItineraireCreationDTO dto2 = new ItineraireCreationDTO(CORRECT_NAME, new String[]{"-1", "-5", String.valueOf(Integer.MAX_VALUE)}, CORRECT_DISTANCE);
        Itineraire itineraire2 = setItineraryFromDTOAndUser(dto2, user);
        Assertions.assertThrows(DonneesInvalidesException.class, () -> itineraryService.check(itineraire2, user, dto2));

    }

    @Test
    public void itineraireWithClientFromOtherClient() {
        ItineraireCreationDTO dto = new ItineraireCreationDTO(CORRECT_NAME, new String[]{clientFromOtherUser.get_id()}, CORRECT_DISTANCE);
        Itineraire itineraire = setItineraryFromDTOAndUser(dto, user);
        Assertions.assertThrows(DonneesInvalidesException.class, () -> itineraryService.check(itineraire, user, dto));
    }

    @Test
    public void correctItineraryTest() {
        String[] idClients = new String[Itineraire.MAX_CLIENTS];
        for (int i = 0; i < Itineraire.MAX_CLIENTS; i++) {
            Client c = configurationSecurityContextTest.getMockClient(user);
            idClients[i] = c.get_id();
        }
        ItineraireCreationDTO dto = new ItineraireCreationDTO(CORRECT_NAME, idClients, CORRECT_DISTANCE);
        Itineraire itinerary = setItineraryFromDTOAndUser(dto, user);
        final Itineraire[] savedItinerary = new Itineraire[1];
        Assertions.assertDoesNotThrow(() -> itineraryService.check(itinerary, user, dto));
        Assertions.assertDoesNotThrow(() -> savedItinerary[0] = itineraryService.createItineraire(dto, user));
        Assertions.assertTrue(itineraryRepository.existsById(savedItinerary[0].getId()));
        itineraryService.deleteItineraire(savedItinerary[0].getId(), user);
        Assertions.assertFalse(itineraryRepository.existsById(savedItinerary[0].getId()));
    }

    /**
     * Teste la modification de l'itinéraire.
     */
    @Test
    public void editItineraryTest() {
        String[] idClients = new String[8];
        for (int i = 0; i < 8; i++) {
            Client c = configurationSecurityContextTest.getMockClient(user);
            idClients[i] = c.get_id();
        }
        ItineraireCreationDTO dto = new ItineraireCreationDTO(CORRECT_NAME, idClients, CORRECT_DISTANCE);
        Itineraire itinerary = setItineraryFromDTOAndUser(dto, user);
        itinerary.setNom("TestCreate");
        Assertions.assertDoesNotThrow(() -> itineraryService.check(itinerary, user, dto));
        Assertions.assertDoesNotThrow(() -> itineraryService.createItineraire(dto, user));

        Client newClient = configurationSecurityContextTest.getMockClient(user);
        dto.idClients()[dto.idClients().length - 1] = newClient.get_id();
        Itineraire itineraryEdit = setItineraryFromDTOAndUser(dto, user);
        itineraryEdit.setNom("Test");
        final Itineraire[] savedEditItinerary = new Itineraire[1];
        Assertions.assertDoesNotThrow(() -> itineraryService.check(itineraryEdit, user, dto));
        Assertions.assertDoesNotThrow(() -> savedEditItinerary[0] = itineraryService.editItineraire(dto, user, itinerary.getId()));

        Assertions.assertNotEquals("TestCreate", savedEditItinerary[0].getNom());
    }

    /**
     * Méthode outil pour créer un itinéraire.
     */
    private Itineraire setItineraryFromDTOAndUser(ItineraireCreationDTO dto, Utilisateur user) {
        return Itineraire.builder().nom(dto.nom()).distance(dto.distance()).utilisateur(user).build();
    }

    /**
     * Teste la génération d'un chemin optimisé.
     */
    @Test
    public void generateOptimizedPathTest() {
        Client[] clients = new Client[8];
        for (int i = 0; i < 8; i++) {
            Client c = configurationSecurityContextTest.getMockClient(user);
            // STUB Mettre en place de vrai coordonnées et vérifié avec le meilleur résultat
            c.setCoordonnees(new Coordonnees(0, 0));
            clients[i] = c;
        }

        ResultatOptimisation optimizedResult = itineraryService.optimizeShortest(List.of(clients), user);
        Assertions.assertTrue(optimizedResult.clients().stream().allMatch(clientId -> doClientsContainsId(List.of(clients), clientId._id())));
        Assertions.assertTrue(optimizedResult.kilometres() >= 0);
        // TODO ajouter des vérifications plus intéressantes
    }

    /**
     * Métbode pour vérifier si au moins un client d'une liste contient l'id donné.
     *
     * @param clientList La liste de client à utiliser lors de la vérification.
     * @param id         L'id à vérifier la présence.
     * @return True si l'id a été trouvé, sinon false.
     */
    private boolean doClientsContainsId(List<Client> clientList, String id) {
        boolean contains = false;
        for (Client client : clientList) {
            if (client.get_id().equals(id)) {
                contains = true;
                break;
            }
        }
        return contains;
    }
}
