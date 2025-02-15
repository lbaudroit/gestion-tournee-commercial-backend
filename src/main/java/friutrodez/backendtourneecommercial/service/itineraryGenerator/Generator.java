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

public class Generator {
    private static final String START_END_ID = "startEnd";
    private static Point startEnd;
    private static BestRoute bestRouteParallelised;

    public BestRoute run(List<Point> points, double startEndLongitude, double startEndLatitude, AvaibleAlgorithm algoVoyageur) {
        points = new ArrayList<>(points);
        Point startEnd = createPointStartEnd(startEndLongitude, startEndLatitude);
        generateDistances(points, startEnd);
        Method algorithme = algoVoyageur.getAlgorithm();
        BestRoute bestRoute = null;
        try {
            bestRoute = (BestRoute) algorithme.invoke(algoVoyageur, points, startEnd);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return bestRoute;
    }

    private static Point createPointStartEnd(double startEndLongitude, double startEndLatitude) {
        startEnd = new Point(START_END_ID, startEndLongitude, startEndLatitude);
        return startEnd;
    }

    private static void generateDistances(List<Point> points, Point startEnd) {
        points.add(startEnd);
        try {
            new ApiRequest().createMatrix(points);
        } catch (WebClientRequestException | WebClientResponseException e) {
            for (Point point : points) {
                for (Point otherPoint : points) {
                    if (!point.equals(otherPoint)) {
                        point.addPoint(otherPoint);
                    }
                }
            }
        }
        points.remove(startEnd);
    }

}
