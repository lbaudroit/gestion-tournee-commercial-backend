package friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.BestRoute;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BruteForceBranchAndBoundParallel implements Algorithm {
    private static Point startEnd;
    private static BestRoute bestRouteParallelised;

    /**
     * Generate the best route
     *
     * @param points   list of points without startEnd
     * @param startEndGiven start and end point
     * @return the best route generated
     */
    public static BestRoute generate(List<Point> points, Point startEndGiven) {
        startEnd = startEndGiven;
        bestRouteParallelised = new BestRoute(null, Integer.MAX_VALUE);
        points.remove(startEnd);
        generateBranchAndBoundParallelRecursively(points, startEnd, new ArrayList<Point>(), 0, 3);
        points.add(startEnd);
        return bestRouteParallelised;
    }

    private static synchronized void setBestRoute(BestRoute bestRoute) {
        if (bestRoute.getDistance() < bestRouteParallelised.getDistance()) {
            bestRouteParallelised = new BestRoute(new ArrayList<>(bestRoute.getPoints()), bestRoute.getDistance());
        }
    }
    private static void generateBranchAndBoundParallelRecursively(List<Point> points, Point currentPoint, List<Point> route, int distanceOfBranch, int nombreDeNiveauxEnParallele) {
        if (nombreDeNiveauxEnParallele > 0) {
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
                            generateBranchAndBoundParallelRecursively(pointsTmp, point, routeTmp, distance, nombreDeNiveauxEnParallele - 1);
                        });
                        futures.add(future);
                    }
                }
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            }
        } else {
            setBestRoute(BruteForceBranchAndBound.generateBranchAndBoundRecursively(
                    points, currentPoint, route, distanceOfBranch, bestRouteParallelised.getDistance()));
        }
    }
}
