package friutrodez.backendtourneecommercial.service.algorithmeVoyageur;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ItineraryGeneratorTest {
    private ItineraryGenerator itineraryGenerator;
    List<Point> pointsExemples;
    double startEndLongitude;
    double startEndLatitude;

    @BeforeEach
    void setUp() {
        itineraryGenerator = new ItineraryGenerator();
        pointsExemples = List.of(
                new Point("1", 2.5731058, 44.3489985),
                new Point("2", 2.576037, 44.3350156),
                new Point("3", 2.7635276, 44.5259912),
                new Point("4", 2.5593095, 44.3300483),
                new Point("5", 3.0621842, 44.1089357),
                new Point("6", 2.575482, 44.3607091),
                new Point("7", 2.8847171, 43.9596889),
                new Point("8", 2.5618809, 44.5392277),
                new Point("9", 2.4414059, 44.4761361),
                new Point("10", 2.8513199, 44.6840223),
                new Point("11", 2.7271058, 44.2792943),
                new Point("12", 2.0415595, 44.2308501),
                new Point("13", 2.0415595, 44.2308501),
                new Point("14", 3.1608165, 44.0076749),
                new Point("15", 3.0721795, 44.3215041),
                new Point("16", 2.240297, 44.3082695),
                new Point("17", 2.2354147, 44.3086837),
                new Point("18", 2.2497527, 44.5640878),
                new Point("19", 3.0777094, 44.1019),
                new Point("20", 2.2882397, 44.4084791)
        );
        startEndLongitude = 2.8847171;
        startEndLatitude = 43.9596889;
    }

    @Test()
    @DisplayName("Vérification du nombre de point retournés")
    void verificationNombreDePoints() {
        List<Point> points = createListOf(8);
        BestRoute test = itineraryGenerator.run(points, startEndLongitude, startEndLatitude);
        assertEquals(8, test.getPoints().size());
        List<Point> points2 = createListOf(6);
        BestRoute test2 = itineraryGenerator.run(points2, startEndLongitude, startEndLatitude);
        assertEquals(6, test2.getPoints().size());
    }

    @Test()
    @DisplayName("Vérification que le résultat est constant peut import l'ordre des points fournis")
    void verificationOrdrePoints() {
        List<Point> points = createListOf(5);
        BestRoute test = itineraryGenerator.run(points, startEndLongitude, startEndLatitude);
        Collections.shuffle(points);
        BestRoute test2 = itineraryGenerator.run(points, startEndLongitude, startEndLatitude);
        assertEquals(test, test2);
    }

    @Test()
    @DisplayName("tmp")
    void tmp() {
        List<Point> points = createListOf(5);
        BestRoute test = itineraryGenerator.run(points, startEndLongitude, startEndLatitude);
        System.out.println(test);
        BestRoute test2 = itineraryGenerator.runParallelised(points, startEndLongitude, startEndLatitude);
        System.out.println(test2);
    }

    @Test()
    @DisplayName("Vérification que les points renvoyées sont les même que les point envoyés juste dans un autre ordre")
    void verificationPoints() {
        List<Point> points = createListOf(5);
        BestRoute test = itineraryGenerator.run(points, startEndLongitude, startEndLatitude);
        assertTrue(test.getPoints().containsAll(points) && test.getPoints().size() == points.size());
    }

    @Test()
    @DisplayName("Benchmark results")
    void benchmark() {
        HashMap<String, Long> results = new HashMap<>();
        System.out.println("------------------ " + AlgoVoyageur.BRUTE_FORCE + " ------------------");
        //results.putAll(benchMark(AlgoVoyageur.BRUTE_FORCE));
        //displayHashMapAsUtf8Table(results);
        System.out.println("------------------ " + AlgoVoyageur.BRUTE_FORCE_BRANCH_AND_BOUND + " ------------------");
        //results.putAll(benchMark(AlgoVoyageur.BRUTE_FORCE_BRANCH_AND_BOUND));
        //displayHashMapAsUtf8Table(results);
    }

    private List<Point> createListOf(int size) {
        //Renvoie une liste de points aléatoire venant de pointsExemples, sans doublons
        List<Point> points = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            Point point = pointsExemples.get(random.nextInt(pointsExemples.size()));
            if (points.contains(point)) {
                i--;
            } else {
                points.add(point);
            }
        }
        return points;
    }

    private void displayHashMapAsUtf8Table(HashMap<String, Long> results) {
        StringBuilder table = new StringBuilder();
        table.append(String.format("%-50s%-30s\n", "Algorithm", "Time (MicroSeconds)"));

        for (Map.Entry<String, Long> entry : results.entrySet()) {
            table.append(String.format("%-50s%-30d\n", entry.getKey(), entry.getValue()));
        }
        System.out.println(table);
    }


    private HashMap<String, Long> benchMark(AlgoVoyageur algoVoyageur) {
        HashMap<String, Long> results = new HashMap<>();
        List<Point> points = createListOf(3);
        results.put("avec 3 points", itineraryGenerator.benchmark(points, startEndLongitude, startEndLatitude, algoVoyageur));
        points = createListOf(5);
        results.put("avec 5 points", itineraryGenerator.benchmark(points, startEndLongitude, startEndLatitude, algoVoyageur));
        points = createListOf(8);
        results.put("avec 8 points", itineraryGenerator.benchmark(points, startEndLongitude, startEndLatitude, algoVoyageur));
        points = createListOf(9);
        results.put("avec 9 points", itineraryGenerator.benchmark(points, startEndLongitude, startEndLatitude, algoVoyageur));
        points = createListOf(10);
        results.put("avec 10 points", itineraryGenerator.benchmark(points, startEndLongitude, startEndLatitude, algoVoyageur));
        return results;
    }
}