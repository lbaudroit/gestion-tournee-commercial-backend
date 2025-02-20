package friutrodez.backendtourneecommercial.controller;

import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Coordonnees;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ParcoursController {

    private final ClientService clientService;

    @Autowired
    public ParcoursController(ClientService clientService) {

        this.clientService = clientService;
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