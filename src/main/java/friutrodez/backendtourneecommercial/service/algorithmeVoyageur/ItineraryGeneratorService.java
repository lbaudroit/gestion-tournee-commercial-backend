package friutrodez.backendtourneecommercial.service.algorithmeVoyageur;

import java.util.List;

public interface ItineraryGeneratorService {
    public void run(List<Point> points, double startEndLongitude, double startEndLatitude);
}
