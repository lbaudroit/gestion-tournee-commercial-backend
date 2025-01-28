package friutrodez.backendtourneecommercial.controller;

import com.fasterxml.jackson.databind.JsonNode;
import friutrodez.backendtourneecommercial.dto.ItineraireCreationDTO;
import friutrodez.backendtourneecommercial.dto.ResultatOptimisation;
import friutrodez.backendtourneecommercial.model.*;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import friutrodez.backendtourneecommercial.repository.mysql.AppartientRepository;
import friutrodez.backendtourneecommercial.repository.mysql.ItineraireRepository;
import friutrodez.backendtourneecommercial.service.ItineraireService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequestMapping(path = "/itineraire/")
@RestController
public class ItineraireControlleur {

    private final ItineraireRepository itineraireRepository;
    private final ItineraireService itineraireService;
    private final ClientMongoTemplate clientMongoTemplate;
    private final AppartientRepository appartientRepository;

    public ItineraireControlleur(ItineraireRepository itineraireRepository, ItineraireService itineraireService, ClientMongoTemplate clientMongoTemplate, AppartientRepository appartientRepository) {
        this.itineraireRepository = itineraireRepository;
        this.itineraireService = itineraireService;
        this.clientMongoTemplate = clientMongoTemplate;
        this.appartientRepository = appartientRepository;
    }

    @GetMapping(path = "recuperer/")
    public ResponseEntity<Itineraire> getUnItineraire(@RequestParam(name = "id") Long id) {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Itineraire> itineraire = itineraireRepository.findItineraireByIdAndUtilisateur(id, utilisateur);
        return itineraire
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(path = "generer/")
    public ResponseEntity<ResultatOptimisation> genererItineraire(@RequestParam("clients") List<Integer> idClients) {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Client> clients = idClients
                .stream()
                .map(i -> clientMongoTemplate.getOneClient(i.toString(), utilisateur.getId().toString()))
                .toList();

        List<Client> clientsCopy = new ArrayList<>(clients);

        return ResponseEntity.ok(itineraireService.optimiserPlusCourt(clientsCopy));
    }

    @Transactional
    @PostMapping(path = "creer/")
    public ResponseEntity<Itineraire> creerItineraire(@RequestBody ItineraireCreationDTO dto) {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // On save l'itinéraire
        Itineraire aSauvegarder = Itineraire.builder()
                .nom(dto.nom())
                .utilisateur(utilisateur)
                .distance(dto.distance())
                .build();
        Itineraire itineraire = itineraireRepository.save(aSauvegarder);

        // Et les liaisons avec les clients
        for (int position = 0 ; position < dto.idClients().length ; position++) {
            String idClient = dto.idClients()[position];
            Appartient appartenance = new Appartient(new AppartientKey(itineraire, idClient), position);
            appartientRepository.save(appartenance);
        }

        return ResponseEntity.ok(itineraire);
    }

}
