package friutrodez.backendtourneecommercial.service.algorithmeVoyageur;

import java.util.List;

public class ItineraryGenerator implements ItineraryGeneratorService {
    private static final String START_END_ID = "startEnd";
    private static Point startEnd;
    @Override
    public void run(List<Point> points, double startEndLongitude, double startEndLatitude) {
        ApiRequest apiRequest = new ApiRequest();
        points.add(createPointStartEnd(startEndLongitude, startEndLatitude));
        generateDistances(points, apiRequest);
    }

    private static Point createPointStartEnd(double startEndLongitude, double startEndLatitude) {
        startEnd = new Point(START_END_ID, startEndLongitude, startEndLatitude);
        return startEnd;
    }

    private static void generateDistances(List<Point> points, ApiRequest apiRequest) {
        for (Point point : points) {
            for (Point otherPoint : points) {
                if (!point.equals(otherPoint)) {
                    point.addPoint(otherPoint, apiRequest);
                }
            }
        }
    }
}
