package friutrodez.backendtourneecommercial.exception;

/**
 * Exception pour indiquer que des données sont invalides
 */
public class DonneesInvalidesException extends RuntimeException {

    public DonneesInvalidesException(String message) {
        super(message);
    }
}
