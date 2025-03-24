package friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.BestRoute;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.NodeV2;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Classe implémentant l'algorithme de Little pour générer le meilleur itinéraire.
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
@SuppressWarnings("DuplicatedCode")
public class LittleV2 implements Algorithm {

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
        points.add(startEndGiven);
        NodeV2 root = new NodeV2(points);

        PriorityQueue<NodeV2> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(NodeV2::getValue));
        priorityQueue.add(root);

        NodeV2 bestNode = null;
        int bestValueSoFar = Integer.MAX_VALUE;

        // Paramètres de surveillance de la mémoire
        int nodeCheckFrequency = 100;
        double memoryThreshold = 0.95; // 95% de la mémoire maximale
        Runtime runtime = Runtime.getRuntime();

        try {
            do {
                // Vérification périodique de la mémoire
                if (priorityQueue.size() % nodeCheckFrequency == 0) {
                    long usedMemory = runtime.totalMemory() - runtime.freeMemory();
                    double memoryUsageRatio = (double) usedMemory / runtime.maxMemory();

                    if (memoryUsageRatio > memoryThreshold) {
                        throw new IllegalStateException("Memory threshold exceeded.");
                    }
                }

                NodeV2 toExpand = priorityQueue.poll();

                if (toExpand == null) break;

                if (toExpand.getSizeMatrix() == 0) {
                    bestNode = toExpand;
                } else {
                    // Suivi de la meilleure solution incomplète trouvée jusqu'à présent
                    if (toExpand.getValue() < bestValueSoFar) {
                        bestValueSoFar = toExpand.getValue();
                    }

                    toExpand.expand();
                    if (toExpand.getLeft() != null) priorityQueue.add(toExpand.getLeft());
                    if (toExpand.getRight() != null) priorityQueue.add(toExpand.getRight());
                }
            } while (bestNode == null);

            assert bestNode != null;
            return getResult(bestNode, startEndGiven);
        } catch (OutOfMemoryError e) {
            throw new IllegalStateException("Memory threshold exceeded.");
        }
    }

    /**
     * Récupère le résultat final de l'itinéraire à partir du meilleur nœud trouvé.
     *
     * @param bestRoute le meilleur nœud trouvé
     * @param startEnd  le point de départ et d'arrivée
     * @return le meilleur itinéraire sous forme d'objet BestRoute
     */
    private static BestRoute getResult(NodeV2 bestRoute, Point startEnd) {
        List<Point> points = new ArrayList<>();
        List<NodeV2> allNodesOnRoute = bestRoute.getAllNodesOnRoute();
        points.add(startEnd);
        for (int i = 1; i < allNodesOnRoute.size(); i++) {
            for (NodeV2 noeud : allNodesOnRoute) {
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

