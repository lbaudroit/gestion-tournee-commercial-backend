package friutrodez.backendtourneecommercial.exception;

/**
 * Exception pour indiquer que des données sont manquantes
 */
public class DonneesManquantesException extends RuntimeException{

    public DonneesManquantesException(String message) {
        super(message);
    }
}
