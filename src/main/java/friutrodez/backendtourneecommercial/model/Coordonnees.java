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
        if (obj instanceof Coordonnees(double latitude1, double longitude1)) {
            return latitude == latitude1 && longitude == longitude1;
        }
        return false;
    }
}
