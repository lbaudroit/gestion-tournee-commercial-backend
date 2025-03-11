package friutrodez.backendtourneecommercial.controller;


import friutrodez.backendtourneecommercial.dto.Message;
import friutrodez.backendtourneecommercial.dto.Nombre;
import friutrodez.backendtourneecommercial.dto.ParcoursDTO;
import friutrodez.backendtourneecommercial.dto.ParcoursReducedDTO;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Coordonnees;
import friutrodez.backendtourneecommercial.model.Parcours;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mongodb.ParcoursMongoTemplate;
import friutrodez.backendtourneecommercial.service.ParcoursService;
import friutrodez.backendtourneecommercial.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final static int PAGE_SIZE = 30;
    private final ParcoursMongoTemplate parcoursMongoTemplate;

    /**
     * Crée un nouveau parcours pour l'utilisateur authentifié.
     *
     * @return ResponseEntity contenant le client créé ou un message d'erreur si la création échoue.
     * @throws IllegalArgumentException si l'objet client est null ou si l'utilisateur n'est pas authentifié.
     */
    @PostMapping(path="")
    public ResponseEntity<Message> createParcours(@RequestBody ParcoursDTO parcoursDTO) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            parcoursService.createParcours(parcoursDTO, String.valueOf(user.getId()));
            return ResponseEntity.ok(new Message("Parcours créé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Message(e.getMessage()));
        }
    }

    /**
     * Récupère le nombre de pages
     *
     * @return Un objet ResponseEntity contenant le nombre de page ou un message d'erreur.
     */
    @GetMapping(path = "count")
    public ResponseEntity<Nombre> getNumberOfClients() {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int pages = parcoursMongoTemplate.getPageCountForUser(String.valueOf(user.getId()), PAGE_SIZE);
        return ResponseEntity.ok(new Nombre(pages));
    }

    /**
     * Récupère une liste paginée de parcours pour l'utilisateur authentifié.
     *
     * @param page Numéro de la page (doit être >= 0).
     * @return ResponseEntity contenant la liste paginée des clients ou un message d'erreur si la récupération échoue.
     */
    @GetMapping(path = "lazy")
    public ResponseEntity<List<ParcoursReducedDTO>> getParcoursLazy(@RequestParam(name = "page") int page) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Récupérer les itinéraires
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        List<ParcoursReducedDTO> parcours = parcoursService.getLazyReducedParcours(String.valueOf(user.getId()), pageable);
        return ResponseEntity.ok(parcours);
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


    @GetMapping
    public ResponseEntity<Parcours> getParcours(@RequestParam(name = "id")String id) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().body(parcoursMongoTemplate.find("_id",id).getFirst());
    }
}
