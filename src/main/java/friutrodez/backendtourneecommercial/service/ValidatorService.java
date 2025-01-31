package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Service s'occupant de la validation des entitées.
 *
 * La validation se fait avec l'aide de jakarta.validation.
 * Il faut utiliser des annotations de jakarta.validation.constraint pour que le validator puisse les détecter.
 * @author
 * Benjamin NICOL
 * Enzo CLUZEL
 * Leïla BAUDROIT
 * Ahmed BRIBACH
 */
@Service
public class ValidatorService {
    @Autowired
    private Validator validator;

    /**
     * Cette méthode vérifie l'objet en paramètre.
     * Cette objet doit être valide sinon une exception sera envoyée.
     * @param object L'objet à valider.
     * @param <T> Le type de l'objet en paramètre.
     */
    public <T> void mustValidate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<T> violation : violations) {
                sb.append(violation.getMessage()).append("\n");
            }
            throw new DonneesInvalidesException(sb.toString());

        }
    }

}
