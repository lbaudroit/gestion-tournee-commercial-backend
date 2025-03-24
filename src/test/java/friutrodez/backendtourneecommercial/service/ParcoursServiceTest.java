package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.dto.ParcoursDTO;
import friutrodez.backendtourneecommercial.dto.ParcoursReducedDTO;
import friutrodez.backendtourneecommercial.model.*;
import friutrodez.backendtourneecommercial.repository.mongodb.ParcoursMongoTemplate;
import friutrodez.backendtourneecommercial.service.utils.ParcoursTestUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.geo.GeoJsonLineString;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour ParcoursServiceTest.
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
@SpringBootTest
@Transactional
@Rollback
public class ParcoursServiceTest {

    @Autowired
    private ParcoursMongoTemplate parcoursMongoTemplate;

    @Autowired
    private ParcoursService parcoursService;

    @BeforeEach
    void setUp() {
        parcoursMongoTemplate.mongoTemplate.dropCollection(Parcours.class);
    }

    @Test
    void createParcoursWithValidData() {
        ParcoursDTO dto = new ParcoursDTO(List.of(new EtapesParcours()), "Parcours Test", new GeoJsonLineString(List.of(new org.springframework.data.geo.Point(0, 0))), LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        String userId = "1";

        assertDoesNotThrow(() -> parcoursService.createParcours(dto, userId));
        List<Parcours> savedParcours = parcoursMongoTemplate.mongoTemplate.findAll(Parcours.class);
        assertFalse(savedParcours.isEmpty());
        assertEquals("Parcours Test", savedParcours.getFirst().getNom());
    }

    @Test
    void getLazyReducedParcoursWithValidData() {
        String userId = "1";
        Pageable pageable = PageRequest.of(0, 10);
        Parcours parcours = Parcours.builder()
                .nom("Parcours Test")
                .etapes(List.of(new EtapesParcours()))
                .chemin(new GeoJsonLineString(List.of(new org.springframework.data.geo.Point(0, 0))))
                .dateDebut(LocalDateTime.now())
                .dateFin(LocalDateTime.now().plusHours(1))
                .idUtilisateur(userId)
                .build();
        parcoursMongoTemplate.save(parcours);

        List<ParcoursReducedDTO> result = parcoursService.getLazyReducedParcours(userId, pageable);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Parcours Test", result.getFirst().nom());
    }

    @Test
    void getLazyReducedParcoursWithNonExistentUserReturnsEmptyList() {
        String nonExistentUserId = "999";
        Pageable pageable = PageRequest.of(0, 10);

        List<ParcoursReducedDTO> result = parcoursService.getLazyReducedParcours(nonExistentUserId, pageable);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Teste la suppression d'un parcours avec des données valides.
     */
    @Test
    void deleteParcoursSuccessfully() {
        String userId = "1";

        // Création du parcours
        ParcoursDTO dto = ParcoursTestUtils.createRandomParcours();
        String parcoursId = parcoursService.createParcours(dto, userId);

        // Vérification qu'il a bien été créé
        assertNotNull(parcoursId);
        assertFalse(parcoursMongoTemplate.mongoTemplate.findAll(Parcours.class).isEmpty());

        // Suppression du parcours
        assertDoesNotThrow(() -> parcoursService.deleteOneParcours(parcoursId, userId));

        // Vérification que le parcours a bien été supprimé
        assertNull(parcoursMongoTemplate.mongoTemplate.findById(parcoursId, Parcours.class));

    }

    /**
     * Teste la suppression d'un parcours avec un ID de parcours invalide.
     */
    @Test
    void deleteOneParcoursWithInvalidParcoursIDThrowsException() {
        String userID = "1";
        String invalidParcoursID = "invalidId";

        assertThrows(NoSuchElementException.class, () -> parcoursService.deleteOneParcours(invalidParcoursID, userID));
    }

    /**
     * Teste la suppression d'un parcours avec un utilisateur non existant.
     */
    @Test
    void deleteOneParcoursWithNonExistentUserThrowsException() {
        String nonExistentUserID = "999L";
        String userID = "1";
        // Création du parcours
        ParcoursDTO dto = ParcoursTestUtils.createRandomParcours();
        String validParcoursId = parcoursService.createParcours(dto, userID);

        assertThrows(NoSuchElementException.class, () -> parcoursService.deleteOneParcours(validParcoursId, nonExistentUserID));
    }


}