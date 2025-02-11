package friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects.TestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LittleTest {

    @Test
    void generate() {
        TestData testData = new TestData();
        Point startEnd = testData.getStartEnd();
        Little.generate(testData.getPointsStatic1(startEnd), startEnd);
    }
}