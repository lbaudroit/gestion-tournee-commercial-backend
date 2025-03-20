package friutrodez.backendtourneecommercial.service.itineraryGenerator.objects;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour la classe BestRoute.
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
class BestRouteTest {

    /**
     * Teste si une instance de BestRoute est égale à elle-même.
     */
    @Test
    void bestRouteEqualsSameObject() {
        List<Point> points = List.of(new Point("A", 1.0, 1.0), new Point("B", 2.0, 2.0));
        BestRoute bestRoute1 = new BestRoute(points, 100);
        assertEquals(bestRoute1, bestRoute1);
    }

    /**
     * Teste si deux instances de BestRoute avec les mêmes valeurs sont égales.
     */
    @Test
    void bestRouteEqualsDifferentObjectSameValues() {
        List<Point> points1 = List.of(new Point("A", 1.0, 1.0), new Point("B", 2.0, 2.0));
        List<Point> points2 = List.of(new Point("A", 1.0, 1.0), new Point("B", 2.0, 2.0));
        BestRoute bestRoute1 = new BestRoute(points1, 100);
        BestRoute bestRoute2 = new BestRoute(points2, 100);
        assertEquals(bestRoute1, bestRoute2);
    }

    /**
     * Teste si deux instances de BestRoute avec des distances différentes ne sont pas égales.
     */
    @Test
    void bestRouteNotEqualsDifferentDistance() {
        List<Point> points = List.of(new Point("A", 1.0, 1.0), new Point("B", 2.0, 2.0));
        BestRoute bestRoute1 = new BestRoute(points, 100);
        BestRoute bestRoute2 = new BestRoute(points, 200);
        assertNotEquals(bestRoute1, bestRoute2);
    }

    /**
     * Teste si deux instances de BestRoute avec différents points ne sont pas égales.
     */
    @Test
    void bestRouteNotEqualsDifferentPoints() {
        List<Point> points1 = List.of(new Point("A", 1.0, 1.0), new Point("B", 2.0, 2.0));
        List<Point> points2 = List.of(new Point("C", 3.0, 3.0), new Point("D", 4.0, 4.0));
        BestRoute bestRoute1 = new BestRoute(points1, 100);
        BestRoute bestRoute2 = new BestRoute(points2, 100);
        assertNotEquals(bestRoute1, bestRoute2);
    }

    /**
     * Teste si deux instances de BestRoute avec les mêmes points mais dans un ordre différent ne sont pas égales.
     */
    @Test
    void bestRouteEqualsReversedPoints() {
        List<Point> points1 = List.of(new Point("A", 1.0, 1.0), new Point("B", 2.0, 2.0));
        List<Point> points2 = List.of(new Point("B", 2.0, 2.0), new Point("A", 1.0, 1.0));
        BestRoute bestRoute1 = new BestRoute(points1, 100);
        BestRoute bestRoute2 = new BestRoute(points2, 100);
        assertNotEquals(bestRoute1, bestRoute2);
    }

    /**
     * Teste la méthode toString de la classe BestRoute.
     */
    @Test
    void bestRouteToString() {
        List<Point> points = List.of(new Point("A", 1.0, 1.0), new Point("B", 2.0, 2.0));
        BestRoute bestRoute = new BestRoute(points, 100);
        assertEquals("BestRoute{points=[Point{id='A', longitude=1.0, latitude=1.0}, " +
                "Point{id='B', longitude=2.0, latitude=2.0}], distance=100}", bestRoute.toString());
    }
}