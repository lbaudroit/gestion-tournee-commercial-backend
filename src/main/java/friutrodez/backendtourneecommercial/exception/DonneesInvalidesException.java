package friutrodez.backendtourneecommercial.exception;

/**
 * Exception pour indiquer que que des données sont invalides
 */
public class DonneesInvalidesException extends RuntimeException{

    DonneesInvalidesException(String message) {
        super(message);
    }
}
