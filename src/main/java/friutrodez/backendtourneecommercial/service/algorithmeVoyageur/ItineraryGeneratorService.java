package friutrodez.backendtourneecommercial.service.algorithmeVoyageur;

import java.util.List;

public interface ItineraryGeneratorService {
    public BestRoute run(List<Point> points, double startEndLongitude, double startEndLatitude);
}
