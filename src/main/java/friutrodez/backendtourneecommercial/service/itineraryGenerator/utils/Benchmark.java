package friutrodez.backendtourneecommercial.service.itineraryGenerator.utils;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms.AvaibleAlgorithm;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Settings;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects.BenchMarkResults;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects.TestData;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe pour exécuter des benchmarks sur différents algorithmes de voyageur de commerce.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
public class Benchmark {
    private static final int MIN_POINTS = 3;
    private static final int MAX_POINTS = 25;
    private static final int MIN_PARALLEL_LEVELS = 1;
    private static final int MAX_PARALLEL_LEVELS = 4;
    private static final long TIMEOUT = 10_000_000_000L; // 10 secondes
    private static final int EXECUTIONS = 100;

    /**
     * Exécute les benchmarks pour tous les algorithmes de voyageur de commerce.
     *
     * @return Les résultats des benchmarks.
     */
    public static BenchMarkResults benchmark() {
        BenchMarkResults results = new BenchMarkResults(getHeaders());
        for (AvaibleAlgorithm algorithm : AvaibleAlgorithm.values()) {
            if (algorithm.equals(AvaibleAlgorithm.BRUTE_FORCE_BRANCH_AND_BOUND_PARALLEL)) {
                runParallelBenchmark(algorithm, results);
            } else {
                results.addLine(algorithm.name(), runBenchmark(algorithm));
            }
        }
        return results;
    }

    /**
     * Exécute les benchmarks pour un algorithme parallèle.
     * Les benchmarks sont exécutés pour différents niveaux de parallélisme.
     * Comme configuré dans les constantes MIN_PARALLEL_LEVELS et MAX_PARALLEL_LEVELS.
     *
     * @param algorithm L'algorithme à tester.
     * @param results Les résultats des benchmarks.
     */
    private static void runParallelBenchmark(AvaibleAlgorithm algorithm, BenchMarkResults results) {
        for (int levels = MIN_PARALLEL_LEVELS; levels <= MAX_PARALLEL_LEVELS; levels++) {
            System.out.printf("Parallel levels: %d (min: %d, max: %d)%n", levels, MIN_PARALLEL_LEVELS, MAX_PARALLEL_LEVELS);
            Settings.setNumberOfParallelLevels(levels);
            results.addLine(algorithm.name() + " " + levels, runBenchmark(algorithm));
        }
    }

    /**
     * Exécute un benchmark pour un algorithme spécifique.
     * Les benchmarks sont exécutés pour différentes tailles de points.
     * Comme configuré dans les constantes MIN_POINTS et MAX_POINTS.
     *
     * @param algorithm L'algorithme à tester.
     * @return Une liste des durées d'exécution pour chaque taille de points.
     */
    private static List<Long> runBenchmark(AvaibleAlgorithm algorithm) {
        List<Long> times = new ArrayList<>();
        boolean tooLong = false;
        // On commence de points plutôt que MIN_POINTS pour éviter les temps fossé,
        // autrement le premier temps pour le premier algorithme est incorrecte. Due à la compilation JIT.
        for (int points = MIN_POINTS - 2; points <= MAX_POINTS && !tooLong; points++) {
            long avgTime = getAverageTime(algorithm, points);
            if (points >= MIN_POINTS) {
                avgTime /= EXECUTIONS;
                if (avgTime >= TIMEOUT / 8) {
                    System.out.println("Next levels will be too long, skipping the rest");
                    tooLong = true;
                }
                times.add(avgTime);
            }
        }
        return times;
    }

    /**
     * Calcule le temps moyen d'exécution pour un algorithme et une taille de points donnée.
     *
     * @param algorithm L'algorithme à tester.
     * @param points Le nombre de points.
     * @return Le temps moyen d'exécution.
     */
    private static long getAverageTime(AvaibleAlgorithm algorithm, int points) {
        long avgTime = 0;
        for (int exec = 0; exec < EXECUTIONS; exec++) {
            System.out.printf("%s %d %d/%d%s%n", algorithm.name(), points, exec + 1, EXECUTIONS,
                    exec != 0 ? String.format(" Estimated remaining time: %.2f seconds", (float) (avgTime / exec) / TIMEOUT * (EXECUTIONS - exec)) : "");
            avgTime += executeAlgorithm(algorithm, points);
        }
        return avgTime;
    }

    /**
     * Exécute un algorithme pour une taille de points donnée.
     *
     * @param algorithm L'algorithme à tester.
     * @param points Le nombre de points.
     * @return Le temps d'exécution.
     */
    private static long executeAlgorithm(AvaibleAlgorithm algorithm, int points) {
        TestData testData = new TestData();
        Point startEnd = testData.getStartEnd();
        List<Point> pointList = testData.getXRandPoints(points, startEnd);
        long startTime = System.nanoTime();
        try {
            algorithm.getAlgorithm().invoke(null, pointList, startEnd);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return System.nanoTime() - startTime;
    }

    /**
     * Génère les en-têtes pour les résultats des benchmarks.
     *
     * @return Une liste des en-têtes.
     */
    private static List<String> getHeaders() {
        List<String> headers = new ArrayList<>();
        headers.add("Algorithm");
        for (int i = MIN_POINTS; i <= MAX_POINTS; i++) {
            headers.add(i + " Nodes");
        }
        return headers;
    }

    /**
     * Point d'entrée principal pour exécuter les benchmarks et écrire les résultats dans un fichier.
     *
     * @param args Les arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        BenchMarkResults results = benchmark();
        System.out.println(results);
        results.writeResultsToFile("benchmark.csv");
    }
}