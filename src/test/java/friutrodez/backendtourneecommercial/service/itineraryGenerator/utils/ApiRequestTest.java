package friutrodez.backendtourneecommercial.service.itineraryGenerator.utils;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour ApiRequest.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
class ApiRequestTest {
    ApiRequest apiRequest;
    ArrayList<Point> points;

    /**
     * Configuration avant chaque test.
     */
    @BeforeEach
    void setUp() {
        apiRequest = new ApiRequest();
        Point startEnd = new Point("startEnd", 2.8847171, 43.9596889);
        Point point1 = new Point("tmp", 2.5732901, 44.3602355);
        Point point2 = new Point("tmp2", 3.0666963, 44.3223815);
        Point point3 = new Point("tmp3", 2.5731058, 44.3489985);
        Point point4 = new Point("tmp4", 2.576037, 44.3350156);
        Point point5 = new Point("tmp5", 2.7635276, 44.5259912);
        Point point6 = new Point("tmp6", 2.5593095, 44.3300483);
        Point point7 = new Point("tmp7", 3.0621842, 44.1089357);
        Point point8 = new Point("tmp8", 2.575482, 44.3607091);
        points = new ArrayList<>();
        points.add(startEnd);
        points.add(point1);
        points.add(point2);
        points.add(point3);
        points.add(point4);
        points.add(point5);
        points.add(point6);
        points.add(point7);
        points.add(point8);
    }

    /**
     * Test de la méthode createMatrix avec des points valides.
     */
    @Test
    void createMatrixWithValidPoints() {
        try {
            apiRequest.createMatrix(points);
            assertNotNull(points.getFirst().getDistances());
            assertFalse(points.getFirst().getDistances().isEmpty());
        } catch (WebClientResponseException e) {
            fail("Request failed with status: " + e.getStatusCode());
        }
    }

    /**
     * Test de la méthode createMatrix avec une liste de points vide.
     */
    @Test
    void createMatrixWithEmptyPoints() {
        List<Point> emptyPoints = new ArrayList<>();
        try {
            apiRequest.createMatrix(emptyPoints);
            fail("Expected an exception to be thrown");
        } catch (WebClientResponseException e) {
            assertEquals(500, e.getStatusCode().value());
        }
    }

    /**
     * Test de la méthode createMatrix avec un seul point.
     */
    @Test
    void createMatrixWithSinglePoint() {
        List<Point> singlePoint = List.of(new Point("single", 2.8847171, 43.9596889));
        try {
            apiRequest.createMatrix(singlePoint);
            fail("Expected an exception to be thrown");
        } catch (WebClientResponseException e) {
            assertEquals(400, e.getStatusCode().value());
        }
    }

    /**
     * Test de la méthode createMatrix avec des coordonnées invalides.
     */
    @Test
    void createMatrixWithInvalidCoordinates() {
        List<Point> invalidPoints = List.of(new Point("invalid", 999.0, 999.0));
        try {
            apiRequest.createMatrix(invalidPoints);
            fail("Expected an exception to be thrown");
        } catch (WebClientResponseException e) {
            assertEquals(400, e.getStatusCode().value());
        }
    }
}