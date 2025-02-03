package friutrodez.backendtourneecommercial.service.algorithmeVoyageur;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItineraryGeneratorTest {

    @Test
    void run() {
        ItineraryGenerator itineraryGenerator = new ItineraryGenerator();
        Point point1 = new Point("tmp", 2.5732901, 44.3602355);
        Point point2 = new Point("tmp2", 3.0666963, 44.3223815);
        Point point3 = new Point("tmp3", 2.5731058, 44.3489985);
        Point point4 = new Point("tmp4", 2.576037, 44.3350156);
        Point point5 = new Point("tmp5", 2.7635276, 44.5259912);
        Point point6 = new Point("tmp6", 2.5593095, 44.3300483);
        Point point7 = new Point("tmp7", 3.0621842, 44.1089357);
        Point point8 = new Point("tmp8", 2.575482, 44.3607091);
        List<Point> points = new ArrayList<>();
        points.add(point1);
        points.add(point2);
        points.add(point3);
        points.add(point4);
        points.add(point5);
        points.add(point6);
        points.add(point7);
        points.add(point8);
        double startEndLongitude = 2.8847171;
        double startEndLatitude = 43.9596889;
        itineraryGenerator.run(points, startEndLongitude, startEndLatitude);

    }
}