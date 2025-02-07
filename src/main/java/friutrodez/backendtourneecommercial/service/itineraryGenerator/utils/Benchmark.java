package friutrodez.backendtourneecommercial.service.itineraryGenerator.utils;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Settings;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects.BenchMarkResults;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects.TestData;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Benchmark {
    private static final int MIN_SIZE_OF_POINTS = 3;
    private static final int MAX_SIZE_OF_POINTS = 10;
    private static final int MIN_NOMBRE_DE_NIVEAUX_EN_PARALLELE = 6;
    private static final int MAX_NOMBRE_DE_NIVEAUX_EN_PARALLELE = 6;

    private static HashMap<Point, HashMap<Point, Integer>> savedDistances = new HashMap<>();

    public static BenchMarkResults benchmark(Point startEnd) {
        BenchMarkResults benchMarkResults = new BenchMarkResults(getHeaders());
        for (AlgoVoyageur algoVoyageur : AlgoVoyageur.values()) {
            if (algoVoyageur.name().contains("PARALLEL")) {
                for (int i = MIN_NOMBRE_DE_NIVEAUX_EN_PARALLELE; i <= MAX_NOMBRE_DE_NIVEAUX_EN_PARALLELE; i++) {
                    Settings.setNombreDeNiveauxEnParallele(i);
                    benchMarkResults.addLine(algoVoyageur.name() + " " + i, benchmarkRun(startEnd, algoVoyageur, benchMarkResults));
                }
            } else {
                benchMarkResults.addLine(algoVoyageur.name(), benchmarkRun(startEnd, algoVoyageur, benchMarkResults));
            }
        }
        return benchMarkResults;
    }

    private static List<Integer> benchmarkRun(Point startEnd, AlgoVoyageur algoVoyageur, BenchMarkResults benchMarkResults) {
        TestData testData = new TestData();
        List<Integer> results = new ArrayList<>();
        for (int i = MIN_SIZE_OF_POINTS; i <= MAX_SIZE_OF_POINTS; i++) {
            long moyenne = 0;
            for (int j = 0; j < 10; j++) {
                System.out.println(algoVoyageur.name() + " " + i + " " + (j + 1) + "/10");
                //List<Point> finalPoints = createListOf(i, points, startEnd);
                startEnd = new Point(startEnd);
                List<Point> finalPoints = testData.getXRandPoints(i, startEnd);
                long startTime = System.nanoTime();
                try {
                    algoVoyageur.getAlgorithm().invoke(null, finalPoints, startEnd);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                long endTime = System.nanoTime();
                moyenne += endTime - startTime;
            }
            results.add((int) (moyenne / 10000));
        }
        System.out.println(algoVoyageur.name() + " done");
        return results;
    }

    private static List<String> getHeaders() {
        List<String> headers = new ArrayList<>();
        headers.add("Algorithme");
        for (int i = MIN_SIZE_OF_POINTS; i <= MAX_SIZE_OF_POINTS; i++) {
            headers.add(i + " Noeuds");
        }
        return headers;
    }
}
