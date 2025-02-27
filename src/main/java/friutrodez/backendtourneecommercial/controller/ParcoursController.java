package friutrodez.backendtourneecommercial.controller;


import friutrodez.backendtourneecommercial.dto.Message;
import friutrodez.backendtourneecommercial.dto.ParcoursDTO;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Coordonnees;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.service.ParcoursService;
import friutrodez.backendtourneecommercial.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
@AllArgsConstructor
public class ParcoursController {

    private final ParcoursService parcoursService;

    private final ClientService clientService;

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

    /**
     * Endpoint pour récupérer les prospects qui se trouvent à moins de 1000 mètres d'une position donnée.
     * @param latitude La latitude de la position.
     * @param longitude La longitude de la position.
     * @return Les clients dans un rayon de 1000 mètres.
     */
    @GetMapping("prospects/notifications")
    public ResponseEntity<List<Client>> getProspectsForNotifications(
            @RequestParam double latitude,
            @RequestParam double longitude) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Coordonnees position = new Coordonnees(latitude, longitude);
        List<Client> foundClients = clientService.getAllProspectsAround(position,String.valueOf(user.getId()));
        return ResponseEntity.ok(foundClients);
    }
}
