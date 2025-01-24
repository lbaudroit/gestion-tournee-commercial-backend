package friutrodez.backendtourneecommercial.controller;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import friutrodez.backendtourneecommercial.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest controlleur de la ressource client.
 */
@RequestMapping(path="/client/")
@RestController
public class ClientControlleur {

    private final ClientMongoTemplate clientMongoTemplate;

    private final ClientService clientService;

    @Autowired
    public ClientControlleur(ClientMongoTemplate clientMongoTemplate, ClientService clientService) {
        this.clientMongoTemplate = clientMongoTemplate;
        this.clientService = clientService;
    }

    @PutMapping(path = "creer")
    public ResponseEntity<Client> creerClient(@RequestBody Client client) {
        Utilisateur utilisateur = (Utilisateur)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(clientService.creerUnClient(client,String.valueOf(utilisateur.getId())));
    }

    @GetMapping
    public  ResponseEntity<List<Client>> getTousClients() {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(clientMongoTemplate.getAllClients(""+utilisateur.getId()));
    }

    @GetMapping(path="recuperer/")
    public ResponseEntity<Client> getUnClient(@RequestParam(name="id") String id) {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(clientMongoTemplate.getOneClient(id, String.valueOf(utilisateur.getId())));
    }

    @DeleteMapping(path = "supprimer/")
    public ResponseEntity<String> supprimerClient(@RequestParam(name = "id") String id) {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        DeleteResult deleteResult = clientMongoTemplate.removeClientsWithId(id,String.valueOf(utilisateur.getId()));
        if(!deleteResult.wasAcknowledged()) {
            return ResponseEntity.badRequest().body("Le client n'a pas été supprimé");
        }
        return  ResponseEntity.ok("Le client a été supprimé");
    }

    @PostMapping(path="modifier/")
    public ResponseEntity<String> modifierClient(@RequestParam(name="id") String id, @RequestBody Client modifications) {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        clientService.modifierUnClient(id,modifications,String.valueOf(utilisateur.getId()));

        return ResponseEntity.ok().body("Le client a été modifié.");
    }
}
