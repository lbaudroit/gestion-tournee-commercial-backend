package friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.BestRoute;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe BruteForceBranchAndBound implémentant l'interface Algorithm.
 * Utilise une approche de force brute avec élagage pour générer le meilleur itinéraire.
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
public class BruteForceBranchAndBound implements Algorithm {

    /**
     * Génère le meilleur itinéraire.
     *
     * @param points        liste de points sans startEnd
     * @param startEndGiven point de départ et d'arrivée
     * @return le meilleur itinéraire généré
     */
    public static BestRoute generate(List<Point> points, Point startEndGiven) {
        BestRoute result = Algorithm.trivialCasesAndVerifyValidity(points, startEndGiven);
        if (result == null) {
            result = generateBranchAndBoundRecursively(new ArrayList<>(points), startEndGiven, new ArrayList<>(),
                    0, Integer.MAX_VALUE, startEndGiven);
        }
        return result;
    }

    /**
     * Méthode permettant de définir le point de départ et d'arrivée.
     * (Utilisé dans le cas de BruteForceBranchAndBoundParallel)
     * Car on n'utilise pas la méthode generate pour générer l'itinéraire dans ce cas.
     *
     * @param startEnd point de départ et d'arrivée
     */
    public static BestRoute generateForParallel(List<Point> points, Point currentPoint, List<Point> route,
                                                int distanceOfBranch, int lowerBound, Point startEnd) {
        if (points.size() == 1) {
            route.add(points.getFirst());
            distanceOfBranch += currentPoint.getDistance(points.getFirst());
        }
        return generateBranchAndBoundRecursively(new ArrayList<>(points), currentPoint, route, distanceOfBranch,
                lowerBound, startEnd);
    }

    /**
     * Génère récursivement le meilleur itinéraire en utilisant l'algorithme de force brute avec élagage.
     *
     * @param points           liste de points à visiter
     * @param currentPoint     point actuel dans l'itinéraire
     * @param route            itinéraire actuel
     * @param distanceOfBranch distance actuelle de l'itinéraire
     * @param lowerBound       borne inférieure de la distance pour l'élagage
     * @param startEnd         point de départ et d'arrivée
     * @return le meilleur itinéraire trouvé
     */
    protected static BestRoute generateBranchAndBoundRecursively(List<Point> points, Point currentPoint, List<Point> route, int distanceOfBranch, int lowerBound, Point startEnd) {
        if (points.size() == 1) {
            return new BestRoute(new ArrayList<>(route), distanceOfBranch + points.getFirst().getDistance(startEnd));
        }
        BestRoute bestRoute = new BestRoute(null, lowerBound);
        points.remove(currentPoint);
        for (Point point : points) {
            route.add(point);
            int newDistance = distanceOfBranch + currentPoint.getDistance(point);
            if (newDistance < lowerBound) {
                BestRoute candidateRoute = generateBranchAndBoundRecursively(new ArrayList<>(points), point, route, newDistance, bestRoute.distance(), startEnd);

                if (candidateRoute.distance() < bestRoute.distance()) {
                    bestRoute = candidateRoute;
                }
            }
            route.remove(point);
        }
        points.add(currentPoint);
        return bestRoute;
    }
}
