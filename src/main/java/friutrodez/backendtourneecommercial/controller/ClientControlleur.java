package friutrodez.backendtourneecommercial.controller;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path="/client/")
@RestController
public class ClientControlleur {


    @Autowired
    ClientMongoTemplate clientMongoTemplate;
    @PutMapping(path = "creer")
    public ResponseEntity<Client> creerClient(@RequestBody Client client) {
        client = clientMongoTemplate.sauvegarder(client);

        return ResponseEntity.ok(client);
    }

    @GetMapping
    public  ResponseEntity<List<Client>> getTousClients() {
       return ResponseEntity.ok(clientMongoTemplate.recupererToutesLesEntitees());
    }

    @GetMapping(path="recuperer/")
    public ResponseEntity<Client> getUnClient(@RequestParam(name="id") String id) {
        return ResponseEntity.ok(clientMongoTemplate.trouverUn("_id",id));
    }

    @PostMapping(path = "recuperer/")
    public ResponseEntity<List<Client>> getClientsDepuis(@RequestBody Client client) {
        return  ResponseEntity.ok(clientMongoTemplate.getEntitesDepuis(client));
    }

    @DeleteMapping(path = "supprimer/")
    public ResponseEntity<String> supprimerClient(@RequestBody Client client) {
        DeleteResult deleteResult = clientMongoTemplate.supprimer(client);
        if(!deleteResult.wasAcknowledged()) {
            return ResponseEntity.badRequest().body("Le client n'a pas été supprimé");
        }
        return  ResponseEntity.ok("Le client a été supprimé");
    }

    @PostMapping(path="modifier/")
    public ResponseEntity<String> modifierClient(@RequestParam(name="id") String id, @RequestBody Client modifications) {
        modifications.set_id(id);
        UpdateResult updateResult = clientMongoTemplate.modifier(modifications,id);
        if(!updateResult.wasAcknowledged()) {
            return ResponseEntity.badRequest().body("Le client avec l'id " + id + " n'a pas été modifié." );
        }
        return ResponseEntity.ok().body("Le client a été modifié.");
    }
}
