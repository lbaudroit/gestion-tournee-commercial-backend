package friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.BestRoute;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;

import java.util.ArrayList;
import java.util.List;

public class BruteForceBranchAndBound implements Algorithm {
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
        return generateBranchAndBoundRecursively(points, startEnd, new ArrayList<Point>(), 0, Integer.MAX_VALUE);
    }

    public static void setStartEnd(Point startEnd) {
        BruteForceBranchAndBound.startEnd = startEnd;
    }

    protected static BestRoute generateBranchAndBoundRecursively(List<Point> points, Point currentPoint, List<Point> route, int distanceOfBranch, int lowerBound) {
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
                    BestRoute distanceTmp = generateBranchAndBoundRecursively(pointsTmp, point, route, distanceOfBranch + currentPoint.getDistance(point), bestRoute.distance());
                    route.remove(point);
                    if (distanceTmp.distance() < bestRoute.distance()) {
                        bestRoute = distanceTmp;
                    }
                }
            }
            points.add(currentPoint);
            return bestRoute;
        }
    }
}
