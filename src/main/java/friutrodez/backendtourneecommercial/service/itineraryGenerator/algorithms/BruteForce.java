package friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.BestRoute;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;

import java.util.ArrayList;
import java.util.List;

public class BruteForce implements Algorithm {

    private static Point startEnd;

    /**
     * Generate the best route
     *
     * @param points   list of points without startEnd
     * @param startEndGiven start and end point
     * @return the best route generated
     */
    public static BestRoute generate(List<Point> points, Point startEndGiven) {
        startEnd = startEndGiven;
        points = new ArrayList<>(points);
        return generateBruteForceRecursively(points, startEnd, new ArrayList<Point>(), 0);
    }

    private static BestRoute generateBruteForceRecursively(List<Point> points, Point currentPoint, List<Point> route, int distanceOfBranch) {
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
                if (distanceTmp.distance() < bestRoute.distance()) {
                    bestRoute = distanceTmp;
                }
            }
            points.add(currentPoint);
            return bestRoute;
        }
    }
}
