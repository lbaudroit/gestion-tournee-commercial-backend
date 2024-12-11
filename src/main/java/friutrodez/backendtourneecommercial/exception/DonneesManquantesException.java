package friutrodez.backendtourneecommercial.exception;

/**
 * Exception pour indiquer que des données sont manquantes
 */
public class DonneesManquantesException extends RuntimeException{

    DonneesManquantesException(String message) {
        super(message);
    }
}
