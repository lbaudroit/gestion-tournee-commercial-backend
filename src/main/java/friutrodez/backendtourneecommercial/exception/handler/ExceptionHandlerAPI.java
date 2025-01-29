package friutrodez.backendtourneecommercial.exception.handler;

import friutrodez.backendtourneecommercial.exception.AdresseInvalideException;
import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.exception.DonneesManquantesException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;

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
    public ResponseEntity<Map<String,String>> gererDonneesInvalidesException(DonneesInvalidesException exception) {
        //exception.printStackTrace();
        return ResponseEntity.badRequest().body(Map.of("message",exception.getMessage()));
    }

    /**
     * Gère les exceptions (Donnees Manquantes Exception)
     * @param exception l'exception d'origine
     * @return une réponse
     */
    @ExceptionHandler(DonneesManquantesException.class)
    public ResponseEntity<Map<String,String>> gererDonneesManquantesException(DonneesManquantesException exception) {
        //exception.printStackTrace();
        return ResponseEntity.badRequest().body(Map.of("message",exception.getMessage()));
    }

    /**
     * Gère l'exception d'erreur de Contrainte non respectée dans le modèle.
     * @param exception l'exception d'origine
     * @return une réponse
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String,String>> gererContrainteNonRespect
    (ConstraintViolationException exception) {
        return ResponseEntity.badRequest().body(Map.of("message",exception.getConstraintViolations().toString()));
    }
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Map<String,String>> gererContrainteNonRespect
    (SQLIntegrityConstraintViolationException exception) {
        return ResponseEntity.badRequest().body(Map.of("message",exception.toString()));
    }
    @ExceptionHandler(AdresseInvalideException.class)
    public ResponseEntity<Map<String,String>> gererAdresseInvalideException
    (AdresseInvalideException exception) {
        return ResponseEntity.badRequest().body(Map.of("message",exception.toString()));
    }

}
