package friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.BestRoute;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;

import java.util.ArrayList;
import java.util.List;

public class BruteForceBranchAndBound implements Algorithm {

    private static Point startEnd;

    /**
     * Génère le meilleur itinéraire.
     *
     * @param points liste de points sans startEnd
     * @param startEndGiven point de départ et d'arrivée
     * @return le meilleur itinéraire généré
     */
    public static BestRoute generate(List<Point> points, Point startEndGiven) {
        startEnd = startEndGiven;
        return generateBranchAndBoundRecursively(new ArrayList<>(points), startEnd, new ArrayList<>(), 0, Integer.MAX_VALUE);
    }

    /**
     * Méthode permettant de définir le point de départ et d'arrivée.
     * (Utilisé dans le cas de BruteForceBranchAndBoundParallel)
     * Car on n'utilise pas la méthode generate pour générer l'itinéraire dans ce cas.
     * @param startEnd point de départ et d'arrivée
     */
    public static void setStartEnd(Point startEnd) {
        BruteForceBranchAndBound.startEnd = startEnd;
    }


    protected static BestRoute generateBranchAndBoundRecursively(List<Point> points, Point currentPoint, List<Point> route, int distanceOfBranch, int lowerBound) {
        if (points.size() == 1) {
            return new BestRoute(new ArrayList<>(route), distanceOfBranch + points.getFirst().getDistance(startEnd));
        }
        points.remove(currentPoint);
        BestRoute bestRoute = new BestRoute(null, lowerBound);
        for (Point point : points) {
            int newDistance = distanceOfBranch + currentPoint.getDistance(point);
            if (newDistance < lowerBound) {
                route.add(point);
                BestRoute candidateRoute = generateBranchAndBoundRecursively(new ArrayList<>(points), point, route, newDistance, bestRoute.distance());
                route.remove(point);

                if (candidateRoute.distance() < bestRoute.distance()) {
                    bestRoute = candidateRoute;
                }
            }
        }
        points.add(currentPoint);
        return bestRoute;
    }
}
