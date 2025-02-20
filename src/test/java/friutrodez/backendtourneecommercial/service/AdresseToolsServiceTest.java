package friutrodez.backendtourneecommercial.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour AdresseToolsServiceTest.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
class AdresseToolsServiceTest {

    /**
     * Teste que l'adresse est validé par l'api du gourvenement.
     */
    @Test
    void validateAdresse() {
        AdresseToolsService adresseToolsService = new AdresseToolsService();
        assertTrue(adresseToolsService.validateAdresse("50 Avenue de Bordeaux", "12000", "Rodez"));
    }
}