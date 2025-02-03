package friutrodez.backendtourneecommercial.service.algorithmeVoyageur;

import lombok.Getter;

import java.util.HashMap;

/**
 * Représente un point avec un identifiant, une longitude, une latitude et une carte des distances vers d'autres points.
 */
@Getter
public class Point {
    private final String id;
    private final double longitude;
    private final double latitude;
    private final HashMap<Point, Double> distances;

    /**
     * Constructeur pour initialiser un point avec un identifiant, une longitude et une latitude.
     *
     * @param id        l'identifiant du point
     * @param longitude la longitude du point
     * @param latitude  la latitude du point
     */
    public Point(String id, double longitude, double latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.distances = new HashMap<>();
    }

    /**
     * Ajoute un point à la carte des distances en calculant la distance euclidienne entre ce point
     * et le point actuel.
     * @param point le point à ajouter
     */
    public void addPoint(Point point, ApiRequest apiRequest) {
        double distance = apiRequest.getDistance(this, point);
        this.distances.put(point, distance);
    }

    /**
     * Récupère la distance entre le point actuel et un autre point.
     *
     * @param point le point pour lequel récupérer la distance
     * @return la distance entre le point actuel et le point spécifié
     */
    public double getDistance(Point point) {
        return this.distances.get(point);
    }
}