package friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.Generator;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.BestRoute;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmTest {

    private Generator itineraryGenerator;
    List<Point> pointsExemples;
    double startEndLongitude;
    double startEndLatitude;


    @BeforeEach
    void setUp() {
        itineraryGenerator = new Generator();
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

    @Test
    void generate() {
        BestRoute tmp = BruteForce.generate(createListOf(5), new Point("startEnd", startEndLongitude, startEndLatitude));
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
}