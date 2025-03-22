package friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour la classe TestData.
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
class TestDataTest {

    /**
     * Teste la méthode getXRandPoints de la classe TestData.
     * Vérifie que la méthode retourne le nombre correct de points
     * et lance une IllegalArgumentException pour des valeurs invalides.
     */
    @Test
    void getXRandPoints() {
        TestData testData = new TestData();
        List<Point> points = testData.getXRandPoints(32, testData.getStartEnd());
        assertEquals(32, points.size());
        assertThrows(IllegalArgumentException.class, () -> testData.getXRandPoints(41, testData.getStartEnd()));
        assertThrows(IllegalArgumentException.class, () -> testData.getXRandPoints(-1, testData.getStartEnd()));
    }
}