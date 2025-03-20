package friutrodez.backendtourneecommercial.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour AdresseToolsServiceTest.
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
class AdresseToolsServiceTest {

    /**
     * Teste que l'adresse est validé par l'api du gourvenement.
     */
    @Test
    void validateAdresse() {
        AdresseToolsService adresseToolsService = new AdresseToolsService();
        assertTrue(adresseToolsService.validateAdresse("50 Avenue de Bordeaux", "12000", "Rodez"));
        assertFalse(adresseToolsService.validateAdresse("50 Avenue de Boaux", "12000", "Rodez"));
        assertFalse(adresseToolsService.validateAdresse("", "1200", "Rodez"));
    }
}