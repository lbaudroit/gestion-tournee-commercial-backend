package friutrodez.backendtourneecommercial.service.algorithmeVoyageur;

import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

public class ItineraryGenerator implements ItineraryGeneratorService {
    private static final String START_END_ID = "startEnd";
    private static Point startEnd;
    private static BestRoute bestRouteParallelised;
    @Override
    public BestRoute run(List<Point> points, double startEndLongitude, double startEndLatitude) {
        points = new ArrayList<>(points);
        points.add(createPointStartEnd(startEndLongitude, startEndLatitude));
        generateDistances(points);
        return generateBruteForceRecursively(points, startEnd, new ArrayList<Point>(), 0);
        //return generateBranchAndBoundRecursively(points, startEnd, new ArrayList<Point>(), 0, Integer.MAX_VALUE);
    }

    private static Point createPointStartEnd(double startEndLongitude, double startEndLatitude) {
        startEnd = new Point(START_END_ID, startEndLongitude, startEndLatitude);
        return startEnd;
    }

    private static void generateDistances(List<Point> points) {
        try {
            new ApiRequest().createMatrix(points);
        } catch (WebClientRequestException | WebClientResponseException e) {
            for (Point point : points) {
                for (Point otherPoint : points) {
                    if (!point.equals(otherPoint)) {
                        point.addPoint(otherPoint);
                    }
                }
            }
        }
    }

    private BestRoute generateBruteForceRecursively(List<Point> points, Point currentPoint, List<Point> route, int distanceOfBranch) {
        if (points.size() == 1) {
            List<Point> routeTmp = new ArrayList<>(route);
            return new BestRoute(routeTmp, distanceOfBranch + points.getFirst().getDistance(startEnd));
        } else {
            points.remove(currentPoint);
            BestRoute bestRoute = new BestRoute(null, Integer.MAX_VALUE);
            for (Point point : points) {
                List<Point> pointsTmp = new ArrayList<>(points);
                route.add(point);
                BestRoute distanceTmp = generateBruteForceRecursively(pointsTmp, point, route, distanceOfBranch + currentPoint.getDistance(point));
                route.remove(point);
                if (distanceTmp.getDistance() < bestRoute.getDistance()) {
                    bestRoute = distanceTmp;
                }
            }
            points.add(currentPoint);
            return bestRoute;
        }
    }

    private BestRoute generateBranchAndBoundRecursively(List<Point> points, Point currentPoint, List<Point> route, int distanceOfBranch, int lowerBound) {
        if (points.size() == 1) {
            List<Point> routeTmp = new ArrayList<>(route);
            return new BestRoute(routeTmp, distanceOfBranch + points.getFirst().getDistance(startEnd));
        } else {
            points.remove(currentPoint);
            BestRoute bestRoute = new BestRoute(null, lowerBound);
            for (Point point : points) {
                if (distanceOfBranch + currentPoint.getDistance(point) < lowerBound) {
                    List<Point> pointsTmp = new ArrayList<>(points);
                    route.add(point);
                    BestRoute distanceTmp = generateBranchAndBoundRecursively(pointsTmp, point, route, distanceOfBranch + currentPoint.getDistance(point), bestRoute.getDistance());
                    route.remove(point);
                    if (distanceTmp.getDistance() < bestRoute.getDistance()) {
                        bestRoute = distanceTmp;
                    }
                }
            }
            points.add(currentPoint);
            return bestRoute;
        }
    }

    public BestRoute runParallelised(List<Point> points, double startEndLongitude, double startEndLatitude) {
        points = new ArrayList<>(points);
        createPointStartEnd(startEndLongitude, startEndLatitude);
        points.add(startEnd);
        generateDistances(points);
        points.remove(startEnd);
        bestRouteParallelised = new BestRoute(null, Integer.MAX_VALUE);
        generateBranchAndBoundParallelRecursively(points, startEnd, new ArrayList<Point>(), 0);
        return bestRouteParallelised;
    }

    private synchronized void setBestRoute(BestRoute bestRoute) {
        if (bestRoute.getDistance() < bestRouteParallelised.getDistance()) {
            bestRouteParallelised = new BestRoute(new ArrayList<>(bestRoute.getPoints()), bestRoute.getDistance());
        }
    }
    private void generateBranchAndBoundParallelRecursively(List<Point> points, Point currentPoint, List<Point> route, int distanceOfBranch) {
        // Condition arrêt
        if (points.size() == 1) {
            List<Point> routeTmp = new ArrayList<>(route);
            routeTmp.add(points.getFirst());
            int distance = distanceOfBranch + currentPoint.getDistance(points.getFirst())+points.getFirst().getDistance(startEnd);
            setBestRoute(new BestRoute(routeTmp, distance));
        } else {
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (Point point : points) {
                List<Point> pointsTmp = new ArrayList<>(points);
                pointsTmp.remove(point);
                List<Point> routeTmp = new ArrayList<>(route);
                routeTmp.add(point);
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                        generateBranchAndBoundParallelRecursively(pointsTmp, point, routeTmp, distanceOfBranch + currentPoint.getDistance(point));
                });
                futures.add(future);
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }
    }

    protected long benchmark(List<Point> points, double startEndLongitude, double startEndLatitude, AlgoVoyageur algoVoyageur) {
        points = new ArrayList<>(points);
        points.add(createPointStartEnd(startEndLongitude, startEndLatitude));
        generateDistances(points);
        Collections.shuffle(points);
        long moyenne = 0;
        int nombreEssais = 100;
        for (int i = 0; i < nombreEssais; i++) {
            long startTime = System.nanoTime();
            switch(algoVoyageur) {
                case BRUTE_FORCE:
                    generateBruteForceRecursively(points, startEnd, new ArrayList<Point>(), 0);
                    break;
                case BRUTE_FORCE_BRANCH_AND_BOUND:
                    generateBranchAndBoundRecursively(points, startEnd, new ArrayList<Point>(), 0, Integer.MAX_VALUE);
                    break;
                default:
                    generateBruteForceRecursively(points, startEnd, new ArrayList<Point>(), 0);
                    break;
            }
            long endTime = System.nanoTime();
            moyenne += endTime - startTime;
        }
        return moyenne / nombreEssais / 1000 ;
    }

}
