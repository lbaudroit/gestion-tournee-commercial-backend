package friutrodez.backendtourneecommercial.service.itineraryGenerator.utils;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms.BruteForceBranchAndBoundParallel;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Settings;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects.BenchMarkResults;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects.TestData;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Benchmark {
    private static final int MIN_SIZE_OF_POINTS = 3;
    private static final int MAX_SIZE_OF_POINTS = 25;
    private static final int MIN_NOMBRE_DE_NIVEAUX_EN_PARALLELE = 1;
    private static final int MAX_NOMBRE_DE_NIVEAUX_EN_PARALLELE = 4;
    private static final long TIMEOUT = 20_000;
    private static final int NUMBER_OF_EXECUTIONS = 10;

    public static BenchMarkResults benchmark(Point startEnd) {
        BenchMarkResults benchMarkResults = new BenchMarkResults(getHeaders());
        for (AlgoVoyageur algoVoyageur : AlgoVoyageur.values()) {
            if (algoVoyageur.name().contains("PARALLEL")) {
                for (int i = MIN_NOMBRE_DE_NIVEAUX_EN_PARALLELE; i <= MAX_NOMBRE_DE_NIVEAUX_EN_PARALLELE; i++) {
                    System.out.println("Nombre de niveaux en parallèle : " + i + " avec bornes : " + MIN_NOMBRE_DE_NIVEAUX_EN_PARALLELE + " et " + MAX_NOMBRE_DE_NIVEAUX_EN_PARALLELE);
                    Settings.setNombreDeNiveauxEnParallele(i);
                    benchMarkResults.addLine(algoVoyageur.name() + " " + i, benchmarkRun(startEnd, algoVoyageur, benchMarkResults));
                }
            } else {
                benchMarkResults.addLine(algoVoyageur.name(), benchmarkRun(startEnd, algoVoyageur, benchMarkResults));
            }
        }
        return benchMarkResults;
    }

    private static List<Long> benchmarkRun(Point startEnd, AlgoVoyageur algoVoyageur, BenchMarkResults benchMarkResults) {
        TestData testData = new TestData();
        List<Long> results = new ArrayList<>();
        boolean tooLong = false;
        for (int i = MIN_SIZE_OF_POINTS - 2; i <= MAX_SIZE_OF_POINTS && !tooLong; i++) {
            long moyenne = 0;
            for (int j = 0; j < NUMBER_OF_EXECUTIONS; j++) {
                System.out.println(moyenne);
                System.out.println(algoVoyageur.name() + " " + i + " " + (j + 1) + "/" + NUMBER_OF_EXECUTIONS + (j != 0 ? String.format(" Estimation du temps restant : %.2f secondes", ((float)(moyenne / j) / 1_000 * (NUMBER_OF_EXECUTIONS - j))) : ""));
                startEnd = new Point(startEnd);
                List<Point> finalPoints = testData.getXRandPoints(i, startEnd);
                Point finalStartEnd = startEnd;
                long startTime = System.currentTimeMillis();
                try {
                    algoVoyageur.getAlgorithm().invoke(null, finalPoints, finalStartEnd);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime; // Convert to milliseconds
                if ((!tooLong && duration > TIMEOUT / 8)) { // Check if the first task took more than 5 seconds

                }
                moyenne += duration; // Adjust the calculation as needed
            }
            if (i >= MIN_SIZE_OF_POINTS) {
                moyenne = moyenne / NUMBER_OF_EXECUTIONS;
                if (moyenne >= TIMEOUT / 8) {
                    results.add(null);
                    System.out.println("Next levels will be too long, Skipping all the next ones");
                    tooLong = true;
                }
                if (moyenne >= 0) {
                    results.add(moyenne);
                }
            }
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
