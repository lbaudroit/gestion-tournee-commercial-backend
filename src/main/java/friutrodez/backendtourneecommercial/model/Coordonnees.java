package friutrodez.backendtourneecommercial.model;

/**
 * Coordonnée d'un client et de l'utilisateur
 *
 * @param latitude
 * @param longitude
 */
public record Coordonnees(double latitude, double longitude) {

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coordonnees coordonnees) {
            return latitude == coordonnees.latitude && longitude == coordonnees.longitude;
        }
        return false;
    }
}
