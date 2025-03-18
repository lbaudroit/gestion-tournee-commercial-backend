package friutrodez.backendtourneecommercial.controller;

import friutrodez.backendtourneecommercial.dto.*;
import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.model.Appartient;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Itineraire;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import friutrodez.backendtourneecommercial.repository.mysql.AppartientRepository;
import friutrodez.backendtourneecommercial.repository.mysql.ItineraireRepository;
import friutrodez.backendtourneecommercial.service.ItineraireService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


/**
 * Contrôleur pour gérer les itinéraires.
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
@RequestMapping(path = "/itineraire/")
@RestController
@AllArgsConstructor
public class ItineraireController {

    private static final Logger log = LoggerFactory.getLogger(ItineraireController.class);
    private final static int PAGE_SIZE = 30;
    ItineraireRepository itineraireRepository;
    private AppartientRepository appartientRepository;
    private ItineraireService itineraireService;
    private ClientMongoTemplate clientMongoTemplate;

    /**
     * Récupère le nombre d'itinéraires pour l'utilisateur connecté.
     *
     * @return ResponseEntity contenant le nombre d'itinéraires.
     */
    @GetMapping(path = "count")
    public ResponseEntity<Nombre> getItinerairesCount() {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Récupérer le nombre d'itinéraire
        long counted = itineraireRepository.countItineraireByUtilisateur(user);
        Nombre nombre = new Nombre((int) Math.ceil(counted / (double) PAGE_SIZE));
        return ResponseEntity.ok(nombre);
    }

    /**
     * Récupère les itinéraires de manière paginée pour l'utilisateur connecté.
     *
     * @param page Le numéro de la page à récupérer.
     * @return ResponseEntity contenant la liste des itinéraires.
     */
    @GetMapping(path = "lazy")
    public ResponseEntity<List<Itineraire>> getItinerairesLazy(@RequestParam(name = "page") int page) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Récupérer les itinéraires
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return ResponseEntity.ok(itineraireRepository.getItinerairesByUtilisateur(user, pageable));
    }

    /**
     * Récupère un itinéraire spécifique par son identifiant pour l'utilisateur connecté.
     *
     * @param id L'identifiant de l'itinéraire.
     * @return ResponseEntity contenant l'itinéraire ou une réponse 404 si non trouvé.
     */
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
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        ItineraireDTO dto = new ItineraireDTO(id, actualItinerary.getNom(), clients, actualItinerary.getDistance());

        return ResponseEntity.ok(dto);
    }

    /**
     * Génère un itinéraire optimal basé sur une liste de clients.
     *
     * @param idClients La liste des identifiants des clients.
     * @return ResponseEntity contenant le résultat de l'optimisation.
     */
    @GetMapping(path = "generate")
    public ResponseEntity<ResultatOptimisation> generateOptimalItineraire(@RequestParam("clients") List<String> idClients) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Client> clients = clientMongoTemplate.getAllClientsIn(idClients, String.valueOf(user.getId()));

        if (idClients.size() != clients.size()) {
            throw new DonneesInvalidesException("Un id donné est invalide.");
        }
        List<Client> clientsCopy = new ArrayList<>(clients);

        return ResponseEntity.ok(itineraireService.optimizeShortest(clientsCopy, user));
    }

    /**
     * Crée un nouvel itinéraire.
     *
     * @param dto Les données de création de l'itinéraire.
     * @return ResponseEntity contenant un message de confirmation.
     */
    @PostMapping
    public ResponseEntity<Message> createItineraire(@RequestBody ItineraireCreationDTO dto) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        itineraireService.createItineraire(dto, user);
        return ResponseEntity.ok(new Message("Itinéraire créé"));
    }

    /**
     * Modifie un itinéraire existant.
     *
     * @param id  L'identifiant de l'itinéraire à modifier.
     * @param dto Les nouvelles données de l'itinéraire.
     * @return ResponseEntity contenant un message de confirmation.
     */
    @PutMapping("{id}")
    public ResponseEntity<Message> modifyItineraire(@PathVariable("id") long id, @RequestBody ItineraireCreationDTO dto) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        itineraireService.editItineraire(dto, user, id);
        return ResponseEntity.ok(new Message("Itinéraire modifié"));
    }

    /**
     * Supprime un itinéraire existant.
     *
     * @param itineraire_id L'identifiant de l'itinéraire à supprimer.
     * @return ResponseEntity contenant un message de confirmation ou une erreur.
     */
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
