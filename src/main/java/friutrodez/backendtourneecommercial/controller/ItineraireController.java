package friutrodez.backendtourneecommercial.controller;

import friutrodez.backendtourneecommercial.dto.*;
import friutrodez.backendtourneecommercial.model.Appartient;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Itineraire;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import friutrodez.backendtourneecommercial.repository.mysql.AppartientRepository;
import friutrodez.backendtourneecommercial.repository.mysql.ItineraireRepository;
import friutrodez.backendtourneecommercial.service.ItineraireService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequestMapping(path = "/itineraire/")
@RestController
@AllArgsConstructor
public class ItineraireController {

    private static final Logger log = LoggerFactory.getLogger(ItineraireController.class);

    ItineraireRepository itineraireRepository;

    private AppartientRepository appartientRepository;

    private ItineraireService itineraireService;

    private ClientMongoTemplate clientMongoTemplate;

    private final static int PAGE_SIZE = 30;

    @GetMapping(path = "count")
    public ResponseEntity<Nombre> getItinerairesCount() {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Récupérer le nombre d'itinéraire
        long counted = itineraireRepository.countItineraireByUtilisateur(user);
        Nombre nombre = new Nombre((int) Math.ceil(counted / (double) PAGE_SIZE));
        return ResponseEntity.ok(nombre);
    }

    @GetMapping(path = "lazy")
    public ResponseEntity<List<Itineraire>> getItinerairesLazy(@RequestParam(name = "page") int page) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Récupérer les itinéraires
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return ResponseEntity.ok(itineraireRepository.getItinerairesByUtilisateur(user, pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<ItineraireDTO> getItineraire(@PathVariable("id") Long id) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Itineraire> itinerary = itineraireRepository.findItineraireByIdAndUtilisateur(id, user);
        if (itinerary.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        String idUser = String.valueOf(user.getId());
        Itineraire actualItinerary = itinerary.get();
        List<Client> clients = appartientRepository.findAllByIdEmbedded_Itineraire_Id(id)
                .stream()
                .sorted(Comparator.comparingInt(Appartient::getPosition))
                .map(a -> a.getIdEmbedded().getClientId())
                .map(c -> clientMongoTemplate.getOneClient(c, idUser))
                .toList();

        ItineraireDTO dto = new ItineraireDTO(id, actualItinerary.getNom(), clients, actualItinerary.getDistance());

        return ResponseEntity.ok(dto);
    }

    @GetMapping(path = "generate")
    public ResponseEntity<ResultatOptimisation> generateOptimalItineraire(@RequestParam("clients") List<Integer> idClients) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Client> clients = idClients
                .stream()
                .map(i -> clientMongoTemplate.getOneClient(i.toString(), user.getId().toString()))
                .toList();

        List<Client> clientsCopy = new ArrayList<>(clients);

        return ResponseEntity.ok(itineraireService.optimizeShortest(clientsCopy, user));
    }

    @PostMapping
    public ResponseEntity<Message> createItineraire(@RequestBody ItineraireCreationDTO dto) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        itineraireService.createItineraire(dto, user);
        return ResponseEntity.ok(new Message("Itinéraire créé"));
    }

    @PutMapping("{id}")
    public ResponseEntity<Message> modifyItineraire(@PathVariable("id") long id, @RequestBody ItineraireCreationDTO dto) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        itineraireService.editItineraire(dto, user, id);
        return ResponseEntity.ok(new Message("Itinéraire modifié"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Message> deleteItineraire(@PathVariable("id") int itineraire_id) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            itineraireService.deleteItineraire(itineraire_id, user);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Message("Itinéraire non trouvé"));
        }
        return ResponseEntity.ok(new Message("Itinéraire supprimé"));
    }
}
