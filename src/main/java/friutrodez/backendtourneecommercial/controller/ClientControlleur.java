package friutrodez.backendtourneecommercial.controller;

import com.mongodb.client.result.DeleteResult;
import friutrodez.backendtourneecommercial.dto.Message;
import friutrodez.backendtourneecommercial.dto.Nombre;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import friutrodez.backendtourneecommercial.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest controlleur de la ressource client.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
@RequestMapping(path="/client/")
@RestController
public class ClientControlleur {

    private final ClientMongoTemplate clientMongoTemplate;

    private final ClientService clientService;


    /**
     * Contrôleur pour la gestion des clients.
     *
     * @param clientMongoTemplate Template utilisé pour les requêtes MongoDB.
     * @param clientService Service contenant la logique métier des clients.
     */
    @Autowired
    public ClientControlleur(ClientMongoTemplate clientMongoTemplate, ClientService clientService) {
        this.clientMongoTemplate = clientMongoTemplate;
        this.clientService = clientService;
    }

    /**
     * Cette méthode retourne tous les clients associés à l'utilisateur actuellement authentifié.
     * @return Un objet ResponseEntity contenant une liste de clients (List<Client>) associés à l'utilisateur.
     */
    @GetMapping
    public  ResponseEntity<List<Client>> getTousClients() {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(clientMongoTemplate.getAllClients(String.valueOf(utilisateur.getId())));
    }

    /**
     * Récupère le nombre de page
     * @return Un objet ResponseEntity contenant le nombre de page ou un message d'erreur.
     */
    @GetMapping(path="count")
    public ResponseEntity<Nombre>  getNumberClient() {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int pages = clientMongoTemplate.getPageCountForUser(String.valueOf(utilisateur.getId()));
        return ResponseEntity.ok(new Nombre(pages));
    }

    /**
     * Récupère une liste paginée de clients pour l'utilisateur authentifié.
     *
     * @param page Numéro de la page (doit être >= 0).
     * @return ResponseEntity contenant la liste paginée des clients ou un message d'erreur si la récupération échoue.
     */
    @GetMapping(path = "lazy")
    public ResponseEntity<List<Client>> getClientLazy(@RequestParam(name="page") int page) {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Récupérer les itinéraires
        Pageable pageable = PageRequest.of(page, 30);
       List<Client> clients= clientMongoTemplate.getClientsByPage(String.valueOf(utilisateur.getId()), pageable);
        return ResponseEntity.ok(clients);
    }

    /**
     * Récupère un client spécifique en fonction de son ID.
     *
     * @param id L'identifiant du client à récupérer.
     * @return Un ResponseEntity contenant le client correspondant.
     */
    @GetMapping("{id}")
    public ResponseEntity<Client> getUnClient(@PathVariable(name="id") String id) {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(clientMongoTemplate.getOneClient(id, String.valueOf(utilisateur.getId())));
    }

    /**
     * Crée un nouveau client pour l'utilisateur authentifié.
     *
     * @param client Objet contenant les informations du client à créer.
     * @return ResponseEntity contenant le client créé ou un message d'erreur si la création échoue.
     * @throws IllegalArgumentException si l'objet client est null ou si l'utilisateur n'est pas authentifié.
     */
    @PostMapping
    public ResponseEntity<Client> creerClient(@RequestBody Client client) {
        Utilisateur utilisateur = (Utilisateur)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(clientService.CreateOneClient(client,String.valueOf(utilisateur.getId())));
    }

    /**
     * Modifie un client existant en fonction de son ID.
     *
     * @param id L'identifiant du client à modifier.
     * @param modifications Les nouvelles données du client.
     * @return Un ResponseEntity contenant un message de succès.
     */
    @PutMapping("{id}")
    public ResponseEntity<Message> modifierClient(@PathVariable(name="id") String id, @RequestBody Client modifications) {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        clientService.editOneClient(id,modifications,String.valueOf(utilisateur.getId()));

        return ResponseEntity.ok(new Message("Le client a été modifié."));
    }


    /**
     * Supprime un client spécifique en fonction de son ID.
     *
     * @param id L'identifiant du client à supprimer.
     * @return Un ResponseEntity contenant un message de succès ou d'échec.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Message> supprimerClient(@PathVariable(name = "id") String id) {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        DeleteResult deleteResult = clientMongoTemplate.removeClientsWithId(id,String.valueOf(utilisateur.getId()));
        if(!deleteResult.wasAcknowledged()) {
            return ResponseEntity.badRequest().body(new Message("Le client n'a pas été supprimé"));
        }
        return  ResponseEntity.ok(new Message("Le client a été supprimé."));
    }
}
