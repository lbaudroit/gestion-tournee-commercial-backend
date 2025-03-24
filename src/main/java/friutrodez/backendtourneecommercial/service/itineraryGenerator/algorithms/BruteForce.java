package friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.BestRoute;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe BruteForce implémentant l'interface Algorithm.
 * Utilise une approche de force brute pour générer le meilleur itinéraire.
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
public class BruteForce implements Algorithm {

    private static Point startEnd;

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
            startEnd = startEndGiven;
            result = generateBruteForceRecursively(new ArrayList<>(points), startEnd, new ArrayList<>(), 0);
        }
        return result;
    }

    /**
     * Méthode récursive pour générer le meilleur itinéraire en utilisant la force brute.
     * Cette méthode explore toutes les permutations possibles des points pour trouver l'itinéraire
     * avec la distance totale la plus courte.
     * Condition d'arrêt : il ne reste qu'un seul point à visiter.
     * Alors, on ajoute ce point à l'itinéraire actuel et on calcule la distance totale.
     * Sinon, on explore toutes les possibilités de points suivants.
     *
     * @param points           liste de points restants à visiter
     * @param currentPoint     point actuel dans l'itinéraire
     * @param route            itinéraire actuel
     * @param distanceOfBranch distance totale de l'itinéraire actuel
     * @return le meilleur itinéraire trouvé
     */
    private static BestRoute generateBruteForceRecursively(List<Point> points, Point currentPoint, List<Point> route, int distanceOfBranch) {
        if (points.size() == 1) {
            int totalDistance = distanceOfBranch + points.getFirst().getDistance(startEnd);
            return new BestRoute(new ArrayList<>(route), totalDistance);
        }
        points.remove(currentPoint);
        BestRoute bestRoute = new BestRoute(null, Integer.MAX_VALUE);
        for (Point point : points) {
            route.add(point);
            BestRoute candidateRoute = generateBruteForceRecursively(new ArrayList<>(points), point, route, distanceOfBranch + currentPoint.getDistance(point));
            route.remove(point);
            if (candidateRoute.distance() < bestRoute.distance()) {
                bestRoute = candidateRoute;
            }
        }
        points.add(currentPoint);
        return bestRoute;
    }
}
