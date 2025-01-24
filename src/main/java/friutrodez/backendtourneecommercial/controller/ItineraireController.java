package friutrodez.backendtourneecommercial.controller;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import friutrodez.backendtourneecommercial.dto.Message;
import friutrodez.backendtourneecommercial.dto.Nombre;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Itineraire;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import friutrodez.backendtourneecommercial.repository.mysql.AppartientRepository;
import friutrodez.backendtourneecommercial.repository.mysql.ItineraireRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path="/itineraire/")
@RestController
public class ItineraireController {

    @Autowired
    ItineraireRepository itineraireRepository;
    @Autowired
    private AppartientRepository appartientRepository;

    @GetMapping(path = "nombre/")
    public ResponseEntity<Nombre> getNombreItineraire() {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Récupérer le nombre d'itinéraire

        long counted = itineraireRepository.countItineraireByUtilisateur(utilisateur);
        Nombre nombre = new Nombre((int) Math.ceil(counted / 30.0));
        return ResponseEntity.ok(nombre);
    }

    @GetMapping(path = "lazy/")
    public ResponseEntity<List<Itineraire>> getItineraireLazy(@RequestParam(name="page") int page) {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Récupérer les itinéraires
        Pageable pageable = PageRequest.of(page, 30);
        return ResponseEntity.ok(itineraireRepository.getItinerairesByUtilisateur(utilisateur, pageable));
    }

    @Transactional
    @DeleteMapping(path = "supprimer/")
    public ResponseEntity<Message> supprimerItineraire(@RequestParam(name="id") int itineraire_id) {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            appartientRepository.deleteAppartientByIdEmbedded_Itineraire_UtilisateurAndIdEmbedded_Itineraire(
                    utilisateur, itineraireRepository.findById((long) itineraire_id).get());
            itineraireRepository.deleteById((long) itineraire_id);
        } catch (Exception e) {
            return ResponseEntity.status(409).body(new Message("Itinéraire non trouvé"));
        }
        return ResponseEntity.ok(new Message("Itinéraire supprimé"));
    }
}
