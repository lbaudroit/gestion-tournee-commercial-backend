package friutrodez.backendtourneecommercial.service.itineraryGenerator.objects;

import lombok.Getter;

import java.util.HashMap;

/**
 * Représente un point avec un ID, une longitude, une latitude et un dictionnaire des distances vers d'autres points.
 */
@Getter
public class Point {
    private final String id;
    private final double longitude;
    private final double latitude;
    private final HashMap<Point, Integer> distances = new HashMap<>();

    /**
     * Initialise un point avec un ID, une longitude et une latitude.
     *
     * @param id        l'ID du point
     * @param longitude la longitude du point
     * @param latitude  la latitude du point
     */
    public Point(String id, double longitude, double latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Constructeur de copie pour initialiser un point à partir d'un autre point.
     *
     * @param origin le point d'origine
     */
    public Point(Point origin) {
        this(origin.id, origin.longitude, origin.latitude);
    }

    /**
     * Ajoute un point à la carte des distances en calculant la distance euclidienne entre ce point et le point donné.
     *
     * @param point le point à ajouter
     */
    public void addPoint(Point point) {
        int distance = computeHaversineFormula(point, this);
        this.distances.put(point, distance);
    }

    /**
     * Calcule la distance entre deux points géographiques en utilisant la formule de Haversine.
     *
     * @param destination le point de destination
     * @param start       le point de départ
     * @return la distance entre les deux points en mètres
     */
    private static int computeHaversineFormula(Point start, Point destination) {
        double earthRadiusInKm = 6371.0;
        double deltaLat = Math.toRadians(destination.latitude - start.latitude);
        double deltaLon = Math.toRadians(destination.longitude - start.longitude);
        double startLat = Math.toRadians(start.latitude);
        double endLat = Math.toRadians(destination.latitude);

        double a = Math.pow(Math.sin(deltaLat / 2), 2)
                + Math.cos(startLat) * Math.cos(endLat)
                * Math.pow(Math.sin(deltaLon / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (int) Math.round(earthRadiusInKm * c * 1000);
    }

    /**
     * Récupère la distance entre ce point et un autre point.
     *
     * @param point le point pour obtenir la distance
     * @return la distance entre ce point et le point spécifié
     */
    public int getDistance(Point point) {
        return this.distances.get(point);
    }

    @Override
    public String toString() {
        return "Point{" +
                "id='" + id + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point point = (Point) obj;
        return id.equals(point.id);
    }
}