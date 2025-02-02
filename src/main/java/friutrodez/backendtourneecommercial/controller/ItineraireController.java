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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ItineraireController {

    @Autowired
    ItineraireRepository itineraireRepository;
    @Autowired
    private AppartientRepository appartientRepository;
    @Autowired
    private ItineraireService itineraireService;
    @Autowired
    private ClientMongoTemplate clientMongoTemplate;

    @GetMapping(path = "count")
    public ResponseEntity<Nombre> getNombreItineraire() {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Récupérer le nombre d'itinéraire
        long counted = itineraireRepository.countItineraireByUtilisateur(utilisateur);
        Nombre nombre = new Nombre((int) Math.ceil(counted / 30.0));
        return ResponseEntity.ok(nombre);
    }

    @GetMapping(path = "lazy")
    public ResponseEntity<List<Itineraire>> getItineraireLazy(@RequestParam(name = "page") int page) {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Récupérer les itinéraires
        Pageable pageable = PageRequest.of(page, 30);
        return ResponseEntity.ok(itineraireRepository.getItinerairesByUtilisateur(utilisateur, pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<ItineraireDTO> getUnItineraire(@RequestParam(name = "id") Long id) {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Itineraire> itineraire = itineraireRepository.findItineraireByIdAndUtilisateur(id, utilisateur);
        if (!itineraire.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        String idUtilisateur = String.valueOf(utilisateur.getId());
        Itineraire itineraireValide = itineraire.get();
        List<Client> clients = appartientRepository.findAllByIdEmbedded_Itineraire_Id(id)
                .stream()
                .sorted(Comparator.comparingInt(Appartient::getPosition))
                .map(a -> a.getIdEmbedded().getClientId())
                .map(c -> clientMongoTemplate.getOneClient(c, idUtilisateur))
                .toList();

        ItineraireDTO dto = new ItineraireDTO(id, itineraireValide.getNom(), clients, itineraireValide.getDistance());

        return ResponseEntity.ok(dto);
    }

    @GetMapping(path = "generate")
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
    @PostMapping
    public ResponseEntity<Message> creerItineraire(@RequestBody ItineraireCreationDTO dto) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        itineraireService.createItineraire(dto, user);
        return ResponseEntity.ok(new Message("Itinéraire créé"));
    }

    @Transactional
    @PutMapping("{id}")
    public ResponseEntity<Message> modifierItineraire(@PathVariable("id") long id, @RequestBody ItineraireCreationDTO dto) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        itineraireService.editItineraire(dto, user, id);
        return ResponseEntity.ok(new Message("Itinéraire modifié"));
    }

    @Transactional
    @DeleteMapping("{id}")
    public ResponseEntity<Message> supprimerItineraire(@RequestParam(name = "id") int itineraire_id) {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            appartientRepository.deleteAppartientByIdEmbedded_Itineraire_UtilisateurAndIdEmbedded_Itineraire(
                    utilisateur, itineraireRepository.findById((long) itineraire_id).get());
            itineraireRepository.deleteById((long) itineraire_id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Message("Itinéraire non trouvé"));
        }
        return ResponseEntity.ok(new Message("Itinéraire supprimé"));
    }
}
