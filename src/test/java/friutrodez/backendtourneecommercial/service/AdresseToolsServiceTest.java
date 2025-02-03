package friutrodez.backendtourneecommercial.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdresseToolsServiceTest {

    @Test
    void validateAdresse() {
        AdresseToolsService adresseToolsService = new AdresseToolsService();
        assertTrue(adresseToolsService.validateAdresse("50 Avenue de Bordeaux", "12000", "Rodez"));
    }
}