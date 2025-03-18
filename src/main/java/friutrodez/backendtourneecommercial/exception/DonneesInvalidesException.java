package friutrodez.backendtourneecommercial.exception;

/**
 * Exception pour indiquer que des données sont invalides
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
public class DonneesInvalidesException extends RuntimeException {

    /**
     * Constructeur
     * @param message le message d'erreur
     */
    public DonneesInvalidesException(String message) {
        super(message);
    }
}
