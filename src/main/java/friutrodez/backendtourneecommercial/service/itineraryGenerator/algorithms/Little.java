package friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.BestRoute;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Node;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;

import java.util.ArrayList;
import java.util.List;

public class Little implements Algorithm {

    /**
     * Génère le meilleur itinéraire.
     *
     * @param points liste de points sans startEnd
     * @param startEndGiven point de départ et d'arrivée
     * @return le meilleur itinéraire généré
     */
    public static BestRoute generate(List<Point> points, Point startEnd) {
        boolean run = true;
        points.add(startEnd);
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
        return getResult(bestRoute, startEnd);
    }

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

