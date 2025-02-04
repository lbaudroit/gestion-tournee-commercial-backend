package friutrodez.backendtourneecommercial.exception;

/**
 * Exception pour indiquer que des données sont invalides
 */
public class AdresseInvalideException extends RuntimeException {

    public AdresseInvalideException(String message) {
        super(message);
    }
}
