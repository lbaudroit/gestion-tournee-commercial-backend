package friutrodez.backendtourneecommercial.service.itineraryGenerator.utils;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms.AvailableAlgorithm;
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
    private static int minPoints = 3;
    private static int maxPoints = 25;
    private static int minParallelLevels = 1;
    private static int maxParallelLevels = 4;
    private static long timeout = 5_000_000_000L; // 5 secondes
    private static int executions = 100;

    /**
     * Exécute les benchmarks pour tous les algorithmes de voyageur de commerce.
     *
     * @return Les résultats des benchmarks.
     */
    public static BenchMarkResults benchmark() {
        BenchMarkResults results = new BenchMarkResults(getHeaders());
        for (AvailableAlgorithm algorithm : AvailableAlgorithm.values()) {
            if (algorithm.equals(AvailableAlgorithm.BRUTE_FORCE_BRANCH_AND_BOUND_PARALLEL)) {
                runParallelBenchmark(algorithm, results);
            } else {
                results.addLine(algorithm.name(), runBenchmark(algorithm, algorithm.name()));
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
    private static void runParallelBenchmark(AvailableAlgorithm algorithm, BenchMarkResults results) {
        for (int levels = minParallelLevels; levels <= maxParallelLevels; levels++) {
            System.out.printf("Parallel levels: %d (min: %d, max: %d)%n", levels, minParallelLevels, maxParallelLevels);
            Settings.setNumberOfParallelLevels(levels);
            results.addLine(algorithm.name() + " " + levels, runBenchmark(algorithm, algorithm.name() + "_WITH_DEPTH_" + levels));
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
    private static List<Long> runBenchmark(AvailableAlgorithm algorithm, String name) {
        List<Long> times = new ArrayList<>();
        boolean tooLong = false;
        // On commence de points plutôt que MIN_POINTS pour éviter les temps fossé,
        // autrement le premier temps pour le premier algorithme est incorrecte. Due à la compilation JIT.
        BenchMarkResults algorithmSpecificResults = new BenchMarkResults(getSpecficHeaders(name));
        for (int points = minPoints - 2; points <= maxPoints && !tooLong; points++) {
            List<Long> values = new ArrayList<>();
            long avgTime = getAverageTime(algorithm, points, values);
            if (points >= minPoints) {
                algorithmSpecificResults.addLine(String.valueOf(points), values);
                avgTime /= executions;
                if (avgTime >= timeout) {
                    System.out.println("Next levels will be too long to compute. Stopping there for this algorithm.");
                    tooLong = true;
                }
                times.add(avgTime);
            }
        }
        System.out.println(algorithmSpecificResults.display());
        algorithmSpecificResults.writeResultsToFile(name + ".csv");
        return times;
    }

    /**
     * Calcule le temps moyen d'exécution pour un algorithme et une taille de points donnée.
     *
     * @param algorithm L'algorithme à tester.
     * @param points Le nombre de points.
     * @return Le temps moyen d'exécution.
     */
    private static long getAverageTime(AvailableAlgorithm algorithm, int points, List<Long> values) {
        long avgTime = 0;
        for (int exec = 0; exec < executions; exec++) {
            System.out.printf("%s %d %d/%d%s%n", algorithm.name(), points, exec + 1, executions,
                    exec != 0 ? String.format(" Estimated remaining time: %.2f seconds", (float) (avgTime / exec) / 1_000_000_000 * (executions - exec)) : "");
            long time = executeAlgorithm(algorithm, points);
            values.add(time);
            avgTime += time;
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
    private static long executeAlgorithm(AvailableAlgorithm algorithm, int points) {
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
        for (int i = minPoints; i <= maxPoints; i++) {
            headers.add(i + " Nodes");
        }
        return headers;
    }

    /**
     * Génère les en-têtes pour les résultats plus précis des benchmarks.
     *
     * @return Une liste des en-têtes.
     */
    private static List<String> getSpecficHeaders(String name) {
        List<String> headers = new ArrayList<>();
        headers.add(name);
        for (int i = 1; i <= executions; i++) {
            headers.add("n°" + i);
        }
        return headers;
    }

    /**
     * Point d'entrée principal pour exécuter les benchmarks et écrire les résultats dans un fichier.
     *
     * @param args Les arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("minPoints=")) {
                minPoints = Integer.parseInt(arg.split("=")[1]);
            } else if (arg.startsWith("maxPoints=")) {
                maxPoints = Integer.parseInt(arg.split("=")[1]);
            } else if (arg.startsWith("minParallelLevels=")) {
                minParallelLevels = Integer.parseInt(arg.split("=")[1]);
            } else if (arg.startsWith("maxParallelLevels=")) {
                maxParallelLevels = Integer.parseInt(arg.split("=")[1]);
            } else if (arg.startsWith("timeout=")) {
                timeout = Long.parseLong(arg.split("=")[1]);
            } else if (arg.startsWith("executions=")) {
                executions = Integer.parseInt(arg.split("=")[1]);
            }
        }
        BenchMarkResults results = benchmark();
        System.out.println(results);
        results.writeResultsToFile("benchmark.csv");
    }
}