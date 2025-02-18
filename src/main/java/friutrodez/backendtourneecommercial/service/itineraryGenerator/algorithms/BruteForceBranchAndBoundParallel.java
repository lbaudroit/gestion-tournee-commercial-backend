package friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.BestRoute;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Classe implémentant l'algorithme de force brute avec branchement et bornes en parallèle.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
public class BruteForceBranchAndBoundParallel implements Algorithm {
    private static Point startEnd;
    private static BestRoute bestRouteParallel;
    private static final int MIN_PARALLEL_LEVELS = 1;
    private static final int MAX_PARALLEL_LEVELS = 4;

    /**
     * Génère le meilleur itinéraire en utilisant l'algorithme de force brute avec branchement et bornes en parallèle.
     *
     * @param points Liste des points à visiter.
     * @param startEndGiven Point de départ et d'arrivée.
     * @return Le meilleur itinéraire trouvé.
     */
    public static BestRoute generate(List<Point> points, Point startEndGiven) {
        bestRouteParallel = Algorithm.trivialCasesAndVerifyValidity(points, startEndGiven);
        if (bestRouteParallel == null) {
            startEnd = startEndGiven;
            bestRouteParallel = new BestRoute(null, Integer.MAX_VALUE);
            int parallelLevels = Math.min(MAX_PARALLEL_LEVELS, Math.max(MIN_PARALLEL_LEVELS, Settings.getNumberOfParallelLevels()));
            generateBranchAndBoundParallel(points, startEnd, new ArrayList<>(), 0, parallelLevels);
        }
        return bestRouteParallel;
    }

    /**
     * Met à jour le meilleur itinéraire si le nouvel itinéraire est meilleur.
     *
     * @param route Le nouvel itinéraire à comparer.
     */
    private static synchronized void updateBestRoute(BestRoute route) {
        if (route.distance() < bestRouteParallel.distance()) {
            bestRouteParallel = new BestRoute(new ArrayList<>(route.points()), route.distance());
        }
    }

    /**
     * Génère l'itinéraire en utilisant l'algorithme de branchement et bornes en parallèle.
     *
     * @param points Liste des points à visiter.
     * @param currentPoint Point actuel dans l'itinéraire.
     * @param route Itinéraire actuel.
     * @param branchDistance Distance actuelle de l'itinéraire.
     * @param parallelLevels Niveau de parallélisme.
     */
    private static void generateBranchAndBoundParallel(List<Point> points, Point currentPoint, List<Point> route, int branchDistance, int parallelLevels) {
        if (parallelLevels > route.size()) {
            if (points.size() == 1) {
                endCase(points, currentPoint, route, branchDistance);
            } else {
                normalCase(points, currentPoint, route, branchDistance, parallelLevels);
            }
        } else {
            updateBestRoute(BruteForceBranchAndBound.generateForParallel(points, currentPoint, route, branchDistance,
                    bestRouteParallel.distance(), startEnd));
        }
    }

    /**
     * Cas de fin lorsque tous les points ont été visités.
     *
     * @param points Liste des points restants (un seul point).
     * @param currentPoint Point actuel dans l'itinéraire.
     * @param route Itinéraire actuel.
     * @param branchDistance Distance actuelle de l'itinéraire.
     */
    private static void endCase(List<Point> points, Point currentPoint, List<Point> route, int branchDistance) {
        List<Point> tempRoute = new ArrayList<>(route);
        tempRoute.add(points.getFirst());
        int distance = branchDistance + currentPoint.getDistance(points.getFirst()) + points.getFirst().getDistance(startEnd);
        updateBestRoute(new BestRoute(tempRoute, distance));
    }

    /**
     * Cas normal pour générer les branches en parallèle.
     *
     * @param points Liste des points à visiter.
     * @param currentPoint Point actuel dans l'itinéraire.
     * @param route Itinéraire actuel.
     * @param branchDistance Distance actuelle de l'itinéraire.
     * @param parallelLevels Niveau de parallélisme.
     */
    private static void normalCase(List<Point> points, Point currentPoint, List<Point> route, int branchDistance, int parallelLevels) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (Point point : points) {
            int distance = branchDistance + currentPoint.getDistance(point);
            if (distance < bestRouteParallel.distance()) {
                List<Point> tempPoints = new ArrayList<>(points);
                tempPoints.remove(point);
                List<Point> tempRoute = new ArrayList<>(route);
                tempRoute.add(point);
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> generateBranchAndBoundParallel(tempPoints, point, tempRoute, distance, parallelLevels));
                futures.add(future);
            }
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }
}