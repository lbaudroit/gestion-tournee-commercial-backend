package friutrodez.backendtourneecommercial.service.itineraryGenerator.objects;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BestRouteTest {

    @Test
    void bestRouteEqualsSameObject() {
        List<Point> points = List.of(new Point("A", 1.0, 1.0), new Point("B", 2.0, 2.0));
        BestRoute bestRoute1 = new BestRoute(points, 100);
        assertEquals(bestRoute1, bestRoute1);
    }

    @Test
    void bestRouteEqualsDifferentObjectSameValues() {
        List<Point> points1 = List.of(new Point("A", 1.0, 1.0), new Point("B", 2.0, 2.0));
        List<Point> points2 = List.of(new Point("A", 1.0, 1.0), new Point("B", 2.0, 2.0));
        BestRoute bestRoute1 = new BestRoute(points1, 100);
        BestRoute bestRoute2 = new BestRoute(points2, 100);
        assertEquals(bestRoute1, bestRoute2);
    }

    @Test
    void bestRouteNotEqualsDifferentDistance() {
        List<Point> points = List.of(new Point("A", 1.0, 1.0), new Point("B", 2.0, 2.0));
        BestRoute bestRoute1 = new BestRoute(points, 100);
        BestRoute bestRoute2 = new BestRoute(points, 200);
        assertNotEquals(bestRoute1, bestRoute2);
    }

    @Test
    void bestRouteNotEqualsDifferentPoints() {
        List<Point> points1 = List.of(new Point("A", 1.0, 1.0), new Point("B", 2.0, 2.0));
        List<Point> points2 = List.of(new Point("C", 3.0, 3.0), new Point("D", 4.0, 4.0));
        BestRoute bestRoute1 = new BestRoute(points1, 100);
        BestRoute bestRoute2 = new BestRoute(points2, 100);
        assertNotEquals(bestRoute1, bestRoute2);
    }

    @Test
    void bestRouteEqualsReversedPoints() {
        List<Point> points1 = List.of(new Point("A", 1.0, 1.0), new Point("B", 2.0, 2.0));
        List<Point> points2 = List.of(new Point("B", 2.0, 2.0), new Point("A", 1.0, 1.0));
        BestRoute bestRoute1 = new BestRoute(points1, 100);
        BestRoute bestRoute2 = new BestRoute(points2, 100);
        assertNotEquals(bestRoute1, bestRoute2);
    }
}