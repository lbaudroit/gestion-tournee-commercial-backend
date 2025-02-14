package friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.BestRoute;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Noeud;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;

import java.util.ArrayList;
import java.util.List;

public class Little implements Algorithm {
    public static BestRoute generate(List<Point> points, Point startEnd) {
        boolean run = true;
        points.add(startEnd);
        Noeud racine = new Noeud(points);
        Noeud bestRoute = null;
        while (run) {
            Noeud toExpand = racine.getLowestValueNode();
            if (toExpand.getSizeMatrix() == 0) {
                bestRoute = toExpand.getLowestValueNode();
                run = false;
            } else {
                toExpand.expand();
            }
        }
        return getResult(bestRoute, startEnd);
    }

    private static BestRoute getResult(Noeud bestRoute, Point startEnd) {
        List<Point> points = new ArrayList<>();
        List<Noeud> allNodesOnRoute = bestRoute.getAllNodesOnRoute();
        points.add(startEnd);
        for (int i = 1; i < allNodesOnRoute.size(); i++) {
            for (Noeud noeud : allNodesOnRoute) {
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

