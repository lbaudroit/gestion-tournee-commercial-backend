package friutrodez.backendtourneecommercial.exception;

/**
 * Exception pour indiquer que des données sont manquantes
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
public class DonneesManquantesException extends RuntimeException {

    /**
     * Constructeur
     *
     * @param message le message d'erreur
     */
    public DonneesManquantesException(String message) {
        super(message);
    }
}
