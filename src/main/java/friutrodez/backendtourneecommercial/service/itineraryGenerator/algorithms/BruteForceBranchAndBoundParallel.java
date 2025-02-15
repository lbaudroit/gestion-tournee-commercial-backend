package friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.BestRoute;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BruteForceBranchAndBoundParallel implements Algorithm {
    private static Point startEnd;
    private static BestRoute bestRouteParallelised;
    private static final int NOMBRE_DE_NIVEAUX_EN_PARALLELE_MINIMUM = 1;
    private static final int NOMBRE_DE_NIVEAUX_EN_PARALLELE_MAXIMUM = 4;

    public static BestRoute generate(List<Point> points, Point startEndGiven) {
        startEnd = startEndGiven;
        bestRouteParallelised = new BestRoute(null, Integer.MAX_VALUE);
        points.remove(startEnd);
        int nombreDeNiveauxEnParallele = Settings.getNumberOfParallelLevels();
        if(nombreDeNiveauxEnParallele < NOMBRE_DE_NIVEAUX_EN_PARALLELE_MINIMUM) {
            nombreDeNiveauxEnParallele = NOMBRE_DE_NIVEAUX_EN_PARALLELE_MINIMUM;
        } else if (nombreDeNiveauxEnParallele > NOMBRE_DE_NIVEAUX_EN_PARALLELE_MAXIMUM) {
            nombreDeNiveauxEnParallele = NOMBRE_DE_NIVEAUX_EN_PARALLELE_MAXIMUM;
        }
        generateBranchAndBoundParallelRecursively(points, startEnd, new ArrayList<>(), 0, nombreDeNiveauxEnParallele);
        points.add(startEnd);
        return bestRouteParallelised;
    }

    private static synchronized void setBestRoute(BestRoute bestRoute) {
        if (bestRoute.getDistance() < bestRouteParallelised.getDistance()) {
            bestRouteParallelised = new BestRoute(new ArrayList<>(bestRoute.getPoints()), bestRoute.getDistance());
        }
    }

    private static void generateBranchAndBoundParallelRecursively(List<Point> points, Point currentPoint, List<Point> route, int distanceOfBranch, int nombreDeNiveauxEnParallele) {
        if (nombreDeNiveauxEnParallele > route.size()) {
            if (points.size() == 1) {
                List<Point> routeTmp = new ArrayList<>(route);
                routeTmp.add(points.getFirst());
                int distance = distanceOfBranch + currentPoint.getDistance(points.getFirst()) + points.getFirst().getDistance(startEnd);
                setBestRoute(new BestRoute(routeTmp, distance));
            } else {
                List<CompletableFuture<Void>> futures = new ArrayList<>();
                for (Point point : points) {
                    int distance = distanceOfBranch + currentPoint.getDistance(point);
                    if (distance < bestRouteParallelised.getDistance()) {
                        List<Point> pointsTmp = new ArrayList<>(points);
                        pointsTmp.remove(point);
                        List<Point> routeTmp = new ArrayList<>(route);
                        routeTmp.add(point);
                        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                            generateBranchAndBoundParallelRecursively(pointsTmp, point, routeTmp, distance, nombreDeNiveauxEnParallele);
                        });
                        futures.add(future);
                    }
                }
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            }
        } else {
            BruteForceBranchAndBound.setStartEnd(startEnd);
            setBestRoute(BruteForceBranchAndBound.generateBranchAndBoundRecursively(
                    points, currentPoint, route, distanceOfBranch, bestRouteParallelised.getDistance()));
        }
    }
}