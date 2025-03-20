package friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.BestRoute;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Node;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe implémentant l'algorithme de Little pour générer le meilleur itinéraire.
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
public class Little implements Algorithm {

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
            result = executeLittle(points, startEndGiven);
        }
        return result;
    }

    /**
     * Exécute l'algorithme de Little pour trouver le meilleur itinéraire.
     *
     * @param points        liste de points sans startEnd
     * @param startEndGiven point de départ et d'arrivée
     * @return le meilleur itinéraire trouvé
     */
    private static BestRoute executeLittle(List<Point> points, Point startEndGiven) {
        boolean run = true;
        points.add(startEndGiven);
        Node racine = new Node(points);
        Node bestRoute = null;
        while (run) {
            Node toExpand = racine.getLowestValueNode();
            if (toExpand.getSizeMatrix() == 0) {
                bestRoute = toExpand.getLowestValueNode();
                run = false;
            } else {
                toExpand.expand();
            }
        }
        return getResult(bestRoute, startEndGiven);
    }

    /**
     * Récupère le résultat final de l'itinéraire à partir du meilleur nœud trouvé.
     *
     * @param bestRoute le meilleur nœud trouvé
     * @param startEnd  le point de départ et d'arrivée
     * @return le meilleur itinéraire sous forme d'objet BestRoute
     */
    private static BestRoute getResult(Node bestRoute, Point startEnd) {
        List<Point> points = new ArrayList<>();
        List<Node> allNodesOnRoute = bestRoute.getAllNodesOnRoute();
        points.add(startEnd);
        for (int i = 1; i < allNodesOnRoute.size(); i++) {
            for (Node noeud : allNodesOnRoute) {
                if (noeud.getStart().equals(points.getLast())) {
                    points.add(noeud.getEnd());
                    break;
                }
            }
        }
        points.remove(startEnd);
        return new BestRoute(points, bestRoute.getValue());
    }
}

