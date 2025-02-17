package friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.Generator;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.BestRoute;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class AlgorithmTest {

    @Test
    void tmp() {
        for (int i = 0; i < 100; i++) {
            TestData testData = new TestData();
            Point startEnd = testData.getStartEnd();
            List<Point> points = testData.getStaticPoints3(startEnd);
            try {
                BestRoute bestRoute = (BestRoute) AvaibleAlgorithm.LITTLE.getAlgorithm().invoke(this, points, startEnd);
                System.out.println(bestRoute);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void compareAll() throws InvocationTargetException, IllegalAccessException {
        TestData testData = new TestData();
        Point startEnd = testData.getStartEnd();
        List<Point> points = testData.getStaticPoints3(startEnd);
        System.out.println(AvaibleAlgorithm.BRUTE_FORCE.getAlgorithm().invoke(this, points, startEnd));
        startEnd = testData.getStartEnd();
        points = testData.getStaticPoints3(startEnd);
        System.out.println(AvaibleAlgorithm.BRUTE_FORCE_BRANCH_AND_BOUND.getAlgorithm().invoke(this, points, startEnd));
        startEnd = testData.getStartEnd();
        points = testData.getStaticPoints3(startEnd);
        System.out.println(AvaibleAlgorithm.BRUTE_FORCE_BRANCH_AND_BOUND_PARALLEL.getAlgorithm().invoke(this, points, startEnd));
        startEnd = testData.getStartEnd();
        points = testData.getStaticPoints3(startEnd);
        System.out.println(AvaibleAlgorithm.LITTLE.getAlgorithm().invoke(this, points, startEnd));
    }
}