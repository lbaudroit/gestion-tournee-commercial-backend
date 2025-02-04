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
    private final HashMap<Point, Integer> distances;

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
    public void addPoint(Point point) {
        int distance = computeHarvesineFormula(point,this);
        this.distances.put(point, distance);
    }

    /**
     * Calcule la distance entre deux points géographiques en utilisant la formule de Haversine.
     *
     * La formule de Haversine permet de calculer la distance en ligne droite entre deux
     * points sur une sphère à partir de leurs coordonnées GPS (latitude et longitude).
     * Voir : <a href="https://www.baeldung.com/java-find-distance-between-points">baeldung.com</a>
     * @param destination   Le point de destination.
     * @param start Le point de départ.
     * @return La distance entre les deux points en mètres.
     */
    private static int computeHarvesineFormula(Point destination, Point start) {
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
     * Récupère la distance entre le point actuel et un autre point.
     *
     * @param point le point pour lequel récupérer la distance
     * @return la distance entre le point actuel et le point spécifié
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
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Point point = (Point) obj;
        return id.equals(point.id);
    }
}