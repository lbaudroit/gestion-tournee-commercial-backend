package friutrodez.backendtourneecommercial.exception.handler;

import friutrodez.backendtourneecommercial.dto.Message;
import friutrodez.backendtourneecommercial.exception.AdresseInvalideException;
import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.exception.DonneesManquantesException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Gère les exceptions venant de l'API pour envoyer une réponse personnalisée au client
 * Les exceptions doivent être personnalisées pour être efficace
 */
@ControllerAdvice(basePackages = "friutrodez.backendtourneecommercial.controller")
public class ExceptionHandlerAPI {

    /**
     * Gère les exceptions (Donnees Invalides Exception)
     *
     * @param exception l'exception d'origine
     * @return une réponse
     */
    @ExceptionHandler(DonneesInvalidesException.class)
    public ResponseEntity<Message> gererDonneesInvalidesException(DonneesInvalidesException exception) {
        System.out.println(exception.getClass().toString());
        return ResponseEntity.badRequest().body(new Message(exception.getMessage()));
    }

    /**
     * Gère les exceptions (Donnees Manquantes Exception)
     *
     * @param exception l'exception d'origine
     * @return une réponse
     */
    @ExceptionHandler(DonneesManquantesException.class)
    public ResponseEntity<Message> gererDonneesManquantesException(DonneesManquantesException exception) {
        System.out.println(exception.getClass().toString());
        return ResponseEntity.badRequest().body(new Message(exception.getMessage()));
    }

    /**
     * Gère l'exception d'erreur de Contrainte non respectée dans le modèle.
     *
     * @param exception l'exception d'origine
     * @return une réponse
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Message> gererContrainteNonRespect
    (ConstraintViolationException exception) {
        return ResponseEntity.badRequest().body(new Message(exception.getConstraintViolations().toString()));
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Message> gererContrainteNonRespect
            (SQLIntegrityConstraintViolationException exception) {
        return ResponseEntity.badRequest().body(new Message(exception.toString()));
    }

    @ExceptionHandler(AdresseInvalideException.class)
    public ResponseEntity<Message> gererAdresseInvalideException
            (AdresseInvalideException exception) {
        return ResponseEntity.badRequest().body(new Message(exception.getMessage()));
    }
}
