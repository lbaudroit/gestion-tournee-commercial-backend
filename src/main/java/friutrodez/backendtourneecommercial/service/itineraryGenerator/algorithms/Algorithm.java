package friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.BestRoute;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;

import java.util.List;

/**
 * Interface pour les algorithmes de génération d'itinéraires.
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 *
 */
public interface Algorithm {
    /**
     * Génère le meilleur itinéraire.
     *
     * @param points liste de points sans startEnd
     * @param startEndGiven point de départ et d'arrivée
     * @return le meilleur itinéraire généré
     */
    static BestRoute generate(List<Point> points, Point startEndGiven) {
        BestRoute result = trivialCasesAndVerifyValidity(points, startEndGiven);
        if (result == null) {
            throw new IllegalArgumentException("This method must be overridden.");
        }
        return result;
    }

    /**
     * Handles trivial cases for generating the best route.
     *
     * @param points list of points without startEnd
     * @param startEndGiven starting and ending point
     * @return the best route for trivial cases
     */
    static BestRoute trivialCasesAndVerifyValidity(List<Point> points, Point startEndGiven) {
        if (startEndGiven == null || points == null || points.isEmpty()) {
            throw new IllegalArgumentException("Points and startEndGiven must not be null or empty.");
        }
        if (points.size() == 1) {
            Point singlePoint = points.getFirst();
            int distance = singlePoint.getDistance(startEndGiven) + startEndGiven.getDistance(singlePoint);
            return new BestRoute(points, distance);
        }
        return null;
    }
}
