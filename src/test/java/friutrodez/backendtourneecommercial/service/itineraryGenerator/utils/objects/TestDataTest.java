package friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestDataTest {

    @Test
    void getXRandPoints() {
        TestData testData = new TestData();
        List<Point> points = testData.getXRandPoints(32, testData.getStartEnd());
        assertEquals(23, points.size());
    }
}