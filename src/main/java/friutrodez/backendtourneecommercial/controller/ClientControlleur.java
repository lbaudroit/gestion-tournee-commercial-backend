package friutrodez.backendtourneecommercial.controller;

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
       return ResponseEntity.ok(clientMongoTemplate.recupererToutesLesEntites());
    }
}
