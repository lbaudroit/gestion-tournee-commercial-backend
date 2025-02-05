package friutrodez.backendtourneecommercial.service.itineraryGenerator;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PointTest {

    @Test
    void addPoint() {
        Point point1 = new Point("tmp", 2.5732901, 44.3602355);
        Point point2 = new Point("tmp2", 3.0666963, 44.3223815);
        Point point3 = new Point("tmp3", 2.5731058, 44.3489985);
        point1.addPoint(point2);
        point1.addPoint(point3);

        // Les sites ne calculent pas une précision assez importantes pour la formule de haversine
        Assertions.assertEquals((int)point1.getDistance(point2), 39463);
        Assertions.assertEquals((int)point1.getDistance(point3), 1249);
    }

    @Test
    void getDistance() {
    }
}