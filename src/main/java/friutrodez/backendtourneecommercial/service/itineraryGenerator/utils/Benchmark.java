package friutrodez.backendtourneecommercial.service.itineraryGenerator.utils;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects.BenchMarkResults;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Benchmark {
    private static final int MIN_SIZE_OF_POINTS = 3;
    private static final int MAX_SIZE_OF_POINTS = 10;

    private static HashMap<Point, HashMap<Point, Integer>> savedDistances = new HashMap<>();

    public static BenchMarkResults benchmark(List<Point> points, Point startEnd) {
        BenchMarkResults benchMarkResults = new BenchMarkResults(getHeaders());
        for (AlgoVoyageur algoVoyageur : AlgoVoyageur.values()) {
            List<Integer> results = new ArrayList<>();
            for (int i = MIN_SIZE_OF_POINTS; i <= MAX_SIZE_OF_POINTS; i++) {
                long moyenne = 0;
                for (int j = 0; j < 100; j++) {
                    System.out.println(algoVoyageur.name() + " " + i + " " + j + "/100");
                    List<Point> finalPoints = createListOf(i, points, startEnd);
                    long startTime = System.nanoTime();
                    try {
                        algoVoyageur.getAlgorithm().invoke(null, finalPoints, startEnd);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                    long endTime = System.nanoTime();
                    restorePoints(finalPoints);
                    moyenne += endTime - startTime;
                }
                results.add((int) (moyenne / 100000));
            }
            System.out.println(algoVoyageur.name() + " done");
            benchMarkResults.addLine(algoVoyageur.name(), results);
        }
        return benchMarkResults;
    }

    private static List<Point> createListOf(int size, List<Point> pointsExemples, Point startEnd) {
        if (size > pointsExemples.size()) {
            throw new IllegalArgumentException("Size must be less than pointsExemples size");
        }
        List<Point> points = new ArrayList<>();
        //takes size random points from pointsExemples without duplicates
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            Point point = pointsExemples.get(random.nextInt(pointsExemples.size()));
            if (points.contains(point) || point.equals(startEnd)) {
                i--;
            } else {
                points.add(point);
            }
        }
        points.add(startEnd);
        cleanPoints(points);
        return points;
    }

    private static void cleanPoints(List<Point> points) {
        for (Point point : points) {
            savedDistances.put(point, new HashMap<>(point.getDistances()));
            List<Point> pointsToRemove = new ArrayList<>();
            for (Point otherPoint : point.getDistances().keySet()) {
                if (!points.contains(otherPoint)) {
                    pointsToRemove.add(otherPoint);
                }
            }
            for (Point pointToRemove : pointsToRemove) {
                point.getDistances().remove(pointToRemove);
            }
        }
    }

    private static void restorePoints(List<Point> points) {
        for (Point point : points) {
            point.setDistances(savedDistances.get(point));
        }
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
