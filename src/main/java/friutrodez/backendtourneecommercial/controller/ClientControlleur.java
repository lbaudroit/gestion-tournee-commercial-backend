package friutrodez.backendtourneecommercial.controller;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.security.Security;
import java.util.List;

@RequestMapping(path="/client/")
@RestController
public class ClientControlleur {

    @Autowired
    ClientMongoTemplate clientMongoTemplate;

    @PutMapping(path = "creer")
    public ResponseEntity<Client> creerClient(@RequestBody Client client) {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        client.setIdUtilisateur(String.valueOf(utilisateur.getId()));
        clientMongoTemplate.save(client);
        return ResponseEntity.ok(client);
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
    public ResponseEntity<String> supprimerClient(@RequestBody Client client) {

        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        client.setIdUtilisateur(String.valueOf(utilisateur.getId()));
        DeleteResult deleteResult = clientMongoTemplate.remove(client);
        if(!deleteResult.wasAcknowledged()) {
            return ResponseEntity.badRequest().body("Le client n'a pas été supprimé");
        }
        return  ResponseEntity.ok("Le client a été supprimé");
    }

    @PostMapping(path="modifier/")
    public ResponseEntity<String> modifierClient(@RequestParam(name="id") String id, @RequestBody Client modifications) {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        modifications.setIdUtilisateur(String.valueOf(utilisateur.getId()));

        modifications.set_id(id);
        UpdateResult updateResult = clientMongoTemplate.modifier(modifications,id);
        if(!updateResult.wasAcknowledged()) {
            return ResponseEntity.badRequest().body("Le client avec l'id " + id + " n'a pas été modifié." );
        }
        return ResponseEntity.ok().body("Le client a été modifié.");
    }
}
