package friutrodez.backendtourneecommercial.exception;

/**
 * Exception pour indiquer que des données sont invalides
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
public class AdresseInvalideException extends RuntimeException {

    /**
     * Constructeur
     *
     * @param message le message d'erreur
     */
    public AdresseInvalideException(String message) {
        super(message);
    }
}
