package friutrodez.backendtourneecommercial.controller;

import friutrodez.backendtourneecommercial.dto.Message;
import friutrodez.backendtourneecommercial.dto.ParcoursDTO;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.service.ParcoursService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour la ressource Parcours.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
@RequestMapping(path = "/parcours/")
@RestController
public class ParcoursController {

    private final ParcoursService parcoursService;

    /**
     * Contrôleur pour la gestion des clients.
     *
     * @param parcoursService       Service contenant la logique métier des parcours.
     */
    public ParcoursController(ParcoursService parcoursService) {
        this.parcoursService = parcoursService;
    }

    /**
     * Crée un nouveau client pour l'utilisateur authentifié.
     *
     * @return ResponseEntity contenant le client créé ou un message d'erreur si la création échoue.
     * @throws IllegalArgumentException si l'objet client est null ou si l'utilisateur n'est pas authentifié.
     */
    @PostMapping(path="")
    public ResponseEntity<Message> createParcours(@RequestBody ParcoursDTO parcoursDTO) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            parcoursService.createParcours(parcoursDTO.etapes(), parcoursDTO.nom(), String.valueOf(user.getId()));
            return ResponseEntity.ok(new Message("Parcours créé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Message(e.getMessage()));
        }
    }

}
