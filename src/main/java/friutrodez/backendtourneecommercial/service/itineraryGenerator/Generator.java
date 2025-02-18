package friutrodez.backendtourneecommercial.service.itineraryGenerator;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.BestRoute;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms.AvaibleAlgorithm;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.ApiRequest;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe permettant de générer le meilleur itinéraire.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
public class Generator {
    private static final String START_END_ID = "startEnd";

    /**
     * Exécute l'algorithme pour générer le meilleur itinéraire.
     *
     * @param points Liste des points à visiter.
     * @param startEndLongitude Longitude du point de départ et d'arrivée.
     * @param startEndLatitude Latitude du point de départ et d'arrivée.
     * @param algoVoyageur Algorithme à utiliser pour générer l'itinéraire.
     * @return Le meilleur itinéraire calculé.
     */
    public BestRoute run(List<Point> points, double startEndLongitude, double startEndLatitude, AvaibleAlgorithm algoVoyageur) {
        List<Point> routePoints = new ArrayList<>(points);
        Point startEnd = createPointStartEnd(startEndLongitude, startEndLatitude);
        generateDistances(routePoints, startEnd);

        Method algorithm;
        try {
            algorithm = algoVoyageur.getAlgorithm();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        BestRoute bestRoute;
        try {
            bestRoute = (BestRoute) algorithm.invoke(algoVoyageur, routePoints, startEnd);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return bestRoute;
    }

    /**
     * Crée un point de départ et d'arrivée.
     *
     * @param startEndLongitude Longitude du point de départ et d'arrivée.
     * @param startEndLatitude Latitude du point de départ et d'arrivée.
     * @return Le point de départ et d'arrivée créé.
     */
    private static Point createPointStartEnd(double startEndLongitude, double startEndLatitude) {
        return new Point(START_END_ID, startEndLongitude, startEndLatitude);
    }

    /**
     * Génère les distances entre les points en utilisant une API.
     *
     * @param points Liste des points à visiter.
     * @param startEnd Point de départ et d'arrivée.
     */
    private static void generateDistances(List<Point> points, Point startEnd) {
        points.add(startEnd);
        try {
            new ApiRequest().createMatrix(points);
        } catch (WebClientRequestException | WebClientResponseException e) {
            generateDistancesWithoutApi(points);
        }
        points.remove(startEnd);
    }

    /**
     * Génère les distances entre les points sans utiliser d'API.
     * (Utilisé en cas d'erreur lors de l'appel à l'API)
     *
     * @param points Liste des points à visiter.
     */
    private static void generateDistancesWithoutApi(List<Point> points) {
        for (Point point : points) {
            for (Point other : points) {
                if (!point.equals(other)) {
                    point.addPoint(other);
                }
            }
        }
    }
}
