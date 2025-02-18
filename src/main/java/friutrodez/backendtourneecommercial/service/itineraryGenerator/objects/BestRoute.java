package friutrodez.backendtourneecommercial.service.itineraryGenerator.objects;

import java.util.List;

/**
 * Classe représentant le meilleur itinéraire avec une liste de points et une distance totale.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
public record BestRoute(List<Point> points, int distance) {

    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'objet BestRoute.
     *
     * @return Représentation sous forme de chaîne de caractères de l'objet BestRoute.
     */
    @Override
    public String toString() {
        return "BestRoute{" +
                "points=" + points +
                ", distance=" + distance +
                '}';
    }

}