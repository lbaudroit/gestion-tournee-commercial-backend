package friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.BestRoute;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;

import java.util.List;

/**
 * Interface pour les algorithmes de génération d'itinéraires.
 */
public interface Algorithm {
    /**
     * Génère le meilleur itinéraire.
     *
     * @param points liste de points sans startEnd
     * @param startEndGiven point de départ et d'arrivée
     * @return le meilleur itinéraire généré
     */
    public static BestRoute generate(List<Point> points, Point startEndGiven) {
        return null;
    }
}
