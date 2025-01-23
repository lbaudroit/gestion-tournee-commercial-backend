package friutrodez.backendtourneecommercial.exception.handler;

import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.exception.DonneesManquantesException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Gère les exceptions venant de l'API pour envoyer une réponse personnalisée au client
 * Les exceptions doivent être personnalisées pour être efficace
 */
@ControllerAdvice(basePackages = "friutrodez.backendtourneecommercial.controller")
public class ExceptionHandlerAPI{

    /**
     * Gère les exceptions (Donnees Invalides Exception)
     * @param exception l'exception d'origine
     * @return une réponse
     */
    @ExceptionHandler(DonneesInvalidesException.class)
    public ResponseEntity<String> gererDonneesInvalidesException(DonneesInvalidesException exception) {
        exception.printStackTrace();
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    /**
     * Gère les exceptions (Donnees Manquantes Exception)
     * @param exception l'exception d'origine
     * @return une réponse
     */
    @ExceptionHandler(DonneesManquantesException.class)
    public ResponseEntity<String> gererDonneesManquantesException(DonneesManquantesException exception) {
        exception.printStackTrace();
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

}
