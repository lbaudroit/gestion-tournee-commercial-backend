package friutrodez.backendtourneecommercial.controller;

import friutrodez.backendtourneecommercial.dto.Message;
import friutrodez.backendtourneecommercial.dto.Nombre;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import friutrodez.backendtourneecommercial.service.ClientService;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la ressource client.
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
@RequestMapping(path = "/client/")
@RestController
public class ClientController {

    private static final Logger log = LoggerFactory.getLogger(ClientController.class);
    private final static int PAGE_SIZE = 30;
    private final ClientMongoTemplate clientMongoTemplate;
    private final ClientService clientService;

    /**
     * Contrôleur pour la gestion des clients.
     *
     * @param clientMongoTemplate Template utilisé pour les requêtes MongoDB.
     * @param clientService       Service contenant la logique métier des clients.
     */
    public ClientController(ClientMongoTemplate clientMongoTemplate, ClientService clientService) {
        this.clientMongoTemplate = clientMongoTemplate;
        this.clientService = clientService;
    }

    /**
     * Cette méthode retourne tous les clients associés à l'utilisateur actuellement authentifié.
     *
     * @return Un objet ResponseEntity contenant une liste de clients (List<Client>) associés à l'utilisateur.
     */
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(clientMongoTemplate.getAllClients(String.valueOf(utilisateur.getId())));
    }

    /**
     * Récupère le nombre de pages
     *
     * @return Un objet ResponseEntity contenant le nombre de pages ou un message d'erreur.
     */
    @GetMapping(path = "count")
    public ResponseEntity<Nombre> getNumberOfClients() {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int pages = clientMongoTemplate.getPageCountForUser(String.valueOf(user.getId()));
        return ResponseEntity.ok(new Nombre(pages));
    }

    /**
     * Récupère une liste paginée de clients pour l'utilisateur authentifié.
     *
     * @param page Numéro de la page (doit être >= 0).
     * @return ResponseEntity contenant la liste paginée des clients ou un message d'erreur si la récupération échoue.
     */
    @GetMapping(path = "lazy")
    public ResponseEntity<List<Client>> getClientLazy(@RequestParam(name = "page") int page) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Récupérer les itinéraires
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        List<Client> clients = clientMongoTemplate.getClientsByPage(String.valueOf(user.getId()), pageable);
        return ResponseEntity.ok(clients);
    }

    /**
     * Récupère un client spécifique en fonction de son ID.
     *
     * @param id L'identifiant du client à récupérer.
     * @return Un ResponseEntity contenant le client correspondant.
     */
    @GetMapping("{id}")
    public ResponseEntity<Client> getClient(@PathVariable(name = "id") String id) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Client> client = clientMongoTemplate.getOneClient(id, String.valueOf(user.getId()));

        return client.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Crée un nouveau client pour l'utilisateur authentifié.
     *
     * @param client Objet contenant les informations du client à créer.
     * @return ResponseEntity contenant le client créé ou un message d'erreur si la création échoue.
     * @throws IllegalArgumentException si l'objet client est null ou si l'utilisateur n'est pas authentifié.
     */
    @PostMapping
    public ResponseEntity<Message> createClient(@RequestBody Client client) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        clientService.createOneClient(client, String.valueOf(user.getId()));
        return ResponseEntity.ok(new Message("Le client a été créé."));
    }

    /**
     * Modifie un client existant en fonction de son ID.
     *
     * @param id            L'identifiant du client à modifier.
     * @param modifications Les nouvelles données du client.
     * @return Un ResponseEntity contenant un message de succès.
     */
    @PutMapping("{id}")
    public ResponseEntity<Message> modifyClient(@PathVariable(name = "id") String id, @RequestBody Client modifications) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            clientService.editOneClient(id, modifications, String.valueOf(user.getId()));
            return ResponseEntity.ok(new Message("Le client a été modifié."));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Message("Le client n'a pas été trouvé"));
        }
    }


    /**
     * Supprime un client spécifique en fonction de son ID.
     *
     * @param id L'identifiant du client à supprimer.
     * @return Un ResponseEntity contenant un message de succès ou d'échec.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Message> deleteClient(@PathVariable(name = "id") String id) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            clientService.deleteOneClient(id, user);
            return ResponseEntity.ok(new Message("Le client a été supprimé."));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Message("Le client n'a pas pu être supprimé."));
        }
    }
}
