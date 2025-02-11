package friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.BestRoute;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Noeud;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;

import java.util.List;

public class Little implements Algorithm{
    /**
     * Generate the best route
     *
     * @param points   list of points without startEnd
     * @param startEnd start and end point
     * @return the best route generated
     */
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
        return getResult(bestRoute);
    }

    private static BestRoute getResult(Noeud bestRoute){
        List<Noeud> allNodesOnRoute = bestRoute.getAllNodesOnRoute();

        return new BestRoute(null, bestRoute.getValue());
    }
}
