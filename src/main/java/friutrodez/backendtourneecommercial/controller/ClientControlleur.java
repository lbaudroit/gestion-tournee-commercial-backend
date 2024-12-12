package friutrodez.backendtourneecommercial.controller;

import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
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
       return ResponseEntity.ok(clientMongoTemplate.recupererToutesLesEntites());
    }

    @GetMapping(path="recuperer/")
    public ResponseEntity<Client> getUnClient(@RequestParam(name="id") String id) {
        return ResponseEntity.ok(clientMongoTemplate.trouverUn("_id",id));
    }

    @PostMapping(path = "recuperer/")
    public ResponseEntity<List<Client>> getClientsSpecifique(@RequestBody Client client) {
        return  ResponseEntity.ok(clientMongoTemplate.getClientSpecifique(client));
    }
}
