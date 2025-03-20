package friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.BestRoute;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Classe de test pour les algorithmes de génération d'itinéraires.
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
class AlgorithmsTest {

    TestData testData;

    /**
     * Initialise les données de test avant chaque test.
     */
    @BeforeEach
    void setUp() {
        testData = new TestData();
    }

    /**
     * Teste tous les algorithmes avec des résultats attendus.
     */
    @Test
    void testAllAlgorithmsGoodResults() {
        AvailableAlgorithm[] algorithms = AvailableAlgorithm.values();
        for (AvailableAlgorithm algorithm : algorithms) {
            try {
                // -1, -2, -3 sont des points statiques (toujours les mêmes points)
                assertEquals(150454, runAlgorithm(algorithm, -1).distance());
                assertEquals(140222, runAlgorithm(algorithm, -2).distance());
                assertEquals(217614, runAlgorithm(algorithm, -3).distance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Teste que toutes les méthodes retournent la même distance.
     */
    @Test
    void testAllMethodsReturnTheSameDistance() {
        for (int i = 0; i < 20; i++) {
            for (int sizeOfProblem = 2; sizeOfProblem < 10; sizeOfProblem++) {
                Point startEnd = testData.getStartEnd();
                List<Point> points = new ArrayList<>(testData.getXRandPoints(sizeOfProblem, startEnd));
                BestRoute bestRouteBruteForce = BruteForce.generate(new ArrayList<>(points), startEnd);
                BestRoute bestRouteBruteForceBranchAndBound = BruteForceBranchAndBound.generate(new ArrayList<>(points), startEnd);
                BestRoute bestRouteBruteForceBranchAndBoundParallel = BruteForceBranchAndBoundParallel.generate(new ArrayList<>(points), startEnd);
                BestRoute bestRouteLittle = Little.generate(new ArrayList<>(points), startEnd);
                assertEquals(bestRouteBruteForce.distance(), bestRouteBruteForceBranchAndBound.distance());
                assertEquals(bestRouteBruteForce.distance(), bestRouteLittle.distance());
                assertEquals(bestRouteBruteForce.distance(), bestRouteBruteForceBranchAndBoundParallel.distance());
            }
        }
    }

    /**
     * Teste que la méthode generate de l'interface lance une IllegalArgumentException.
     */
    @Test
    void interfaceGenerateThrowsIllegalArgumentException() {
        Point startEnd = testData.getStartEnd();
        assertThrows(IllegalArgumentException.class, () -> Algorithm.generate(testData.getXRandPoints(5, startEnd), startEnd));
    }

    /**
     * Teste que toutes les méthodes lancent une IllegalArgumentException avec des paramètres null.
     */
    @Test
    void testAllMethodsWithBothNull() {
        assertThrows(IllegalArgumentException.class, () -> Algorithm.generate(null, null));
        assertThrows(IllegalArgumentException.class, () -> BruteForce.generate(null, null));
        assertThrows(IllegalArgumentException.class, () -> BruteForceBranchAndBound.generate(null, null));
        assertThrows(IllegalArgumentException.class, () -> BruteForceBranchAndBoundParallel.generate(null, null));
        assertThrows(IllegalArgumentException.class, () -> Little.generate(null, null));
    }

    /**
     * Teste que toutes les méthodes lancent une IllegalArgumentException avec des points null.
     */
    @Test
    void testAllMethodsWithNullPoints() {
        Point startEnd = testData.getStartEnd();
        assertThrows(IllegalArgumentException.class, () -> Algorithm.generate(null, startEnd));
        assertThrows(IllegalArgumentException.class, () -> BruteForce.generate(null, startEnd));
        assertThrows(IllegalArgumentException.class, () -> BruteForceBranchAndBound.generate(null, startEnd));
        assertThrows(IllegalArgumentException.class, () -> BruteForceBranchAndBoundParallel.generate(null, startEnd));
        assertThrows(IllegalArgumentException.class, () -> Little.generate(null, startEnd));
    }

    /**
     * Teste que toutes les méthodes lancent une IllegalArgumentException avec un point de départ/arrivée null.
     */
    @Test
    void testAllMethodsWithNullStartEnd() {
        Point startEnd = testData.getStartEnd();
        List<Point> points = testData.getXRandPoints(5, startEnd);
        assertThrows(IllegalArgumentException.class, () -> Algorithm.generate(points, null));
        assertThrows(IllegalArgumentException.class, () -> BruteForce.generate(points, null));
        assertThrows(IllegalArgumentException.class, () -> BruteForceBranchAndBound.generate(points, null));
        assertThrows(IllegalArgumentException.class, () -> BruteForceBranchAndBoundParallel.generate(points, null));
        assertThrows(IllegalArgumentException.class, () -> Little.generate(points, null));
    }

    /**
     * Teste que toutes les méthodes lancent une IllegalArgumentException avec une liste de points vide.
     */
    @Test
    void testAllMethodsWithEmpty() {
        Point startEnd = testData.getStartEnd();
        assertThrows(IllegalArgumentException.class, () -> Algorithm.generate(new ArrayList<>(), startEnd));
        assertThrows(IllegalArgumentException.class, () -> BruteForce.generate(new ArrayList<>(), startEnd));
        assertThrows(IllegalArgumentException.class, () -> BruteForceBranchAndBound.generate(new ArrayList<>(), startEnd));
        assertThrows(IllegalArgumentException.class, () -> BruteForceBranchAndBoundParallel.generate(new ArrayList<>(), startEnd));
        assertThrows(IllegalArgumentException.class, () -> Little.generate(new ArrayList<>(), startEnd));
    }

    /**
     * Teste toutes les méthodes avec un seul point.
     */
    @Test
    void testAllMethodsWithSingle() {
        Point startEnd = testData.getStartEnd();
        List<Point> points = testData.getXRandPoints(1, startEnd);
        BestRoute algorithmRoute = Algorithm.generate(points, startEnd);
        BestRoute bruteForceRoute = BruteForce.generate(points, startEnd);
        BestRoute branchAndBoundRoute = BruteForceBranchAndBound.generate(points, startEnd);
        BestRoute parallelRoute = BruteForceBranchAndBoundParallel.generate(points, startEnd);
        BestRoute littleRoute = Little.generate(points, startEnd);

        double expectedDistance = startEnd.getDistance(points.getFirst()) + points.getFirst().getDistance(startEnd);
        assertEquals(expectedDistance, algorithmRoute.distance());
        assertEquals(points, algorithmRoute.points());
        assertEquals(expectedDistance, bruteForceRoute.distance());
        assertEquals(points, bruteForceRoute.points());
        assertEquals(expectedDistance, branchAndBoundRoute.distance());
        assertEquals(points, branchAndBoundRoute.points());
        assertEquals(expectedDistance, parallelRoute.distance());
        assertEquals(points, bruteForceRoute.points());
        assertEquals(expectedDistance, littleRoute.distance());
        assertEquals(points, littleRoute.points());
    }

    /**
     * Teste l'algorithme Little avec un problème plus grand.
     */
    @Test
    void littleWithBiggerProblem() {
        assertEquals(292629, runAlgorithm(AvailableAlgorithm.LITTLE, -4).distance());
        assertEquals(282660, runAlgorithm(AvailableAlgorithm.LITTLE, -5).distance());
    }

    /**
     * Teste l'algorithme Little avec des données invalides.
     */
    @Test
    void littleWithInvalidData() {
        Point startEnd = testData.getStartEnd();
        List<Point> points = testData.getXRandPoints(5, startEnd);
        Point wrongStartEnd = testData.getStartEnd();
        assertThrows(IllegalArgumentException.class, () -> Little.generate(points, wrongStartEnd));
    }

    /**
     * Exécute l'algorithme spécifié avec un nombre donné de points.
     *
     * @param algorithm       L'algorithme à exécuter.
     * @param ammountOfPoints Le nombre de points à utiliser.
     * @return La meilleure route trouvée par l'algorithme.
     */
    private BestRoute runAlgorithm(AvailableAlgorithm algorithm, int ammountOfPoints) {
        Point startEnd = testData.getStartEnd();
        List<Point> points = switch (ammountOfPoints) {
            case -1 -> new ArrayList<>(testData.getStaticPoints1(startEnd));
            case -2 -> new ArrayList<>(testData.getStaticPoints2(startEnd));
            case -3 -> new ArrayList<>(testData.getStaticPoints3(startEnd));
            case -4 -> new ArrayList<>(testData.getStaticPoints4(startEnd));
            case -5 -> new ArrayList<>(testData.getStaticPoints5(startEnd));
            default -> new ArrayList<>(testData.getXRandPoints(ammountOfPoints, startEnd));
        };
        try {
            return (BestRoute) algorithm.getAlgorithm().invoke(algorithm, points, startEnd);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Error while running algorithm");
    }
}