package friutrodez.backendtourneecommercial.controller;

import friutrodez.backendtourneecommercial.dto.Message;
import friutrodez.backendtourneecommercial.dto.Parametrage;
import friutrodez.backendtourneecommercial.exception.AdresseInvalideException;
import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.exception.DonneesManquantesException;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mysql.UtilisateurRepository;
import friutrodez.backendtourneecommercial.service.AuthentificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.tags.Param;

import java.util.Map;

/**
 * Rest Controlleur de la ressource Utilisateur
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
@RequestMapping(path = "/utilisateur/")
@RestController
public class UtilisateurControlleur {

    @Autowired
    UtilisateurRepository utilisateurRepository;
    @Autowired
    private AuthentificationService authentificationService;

    @GetMapping
    public ResponseEntity<Parametrage> recupererUtilisateur() {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Parametrage parametrage = new Parametrage(utilisateur.getNom(), utilisateur.getPrenom(), utilisateur.getEmail());
        return ResponseEntity.ok().body(parametrage);
    }

    /**
     * Crée un compte utilisateur.
     *
     * @param utilisateur Objet contenant les informations du nouvel utilisateur
     * @return ResponseEntity contenant l'utilisateur créé ou un message d'erreur si la création échoue
     */
    @PostMapping
    public ResponseEntity<Map<String,String>> CreerUnCompte(@RequestBody Utilisateur utilisateur) {
        Utilisateur utilisateurCreer =  authentificationService.creerUnCompte(utilisateur);
        if (utilisateurCreer == null) {
            return ResponseEntity.badRequest().body(Map.of("message","Adresse invalide"));
        }
        return ResponseEntity.ok(Map.of("message","L'utilisateur a été créé"));
    }

    @PutMapping
    public ResponseEntity<Message> modifierUtilisateur(@RequestBody Parametrage parametrage) {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        utilisateur.setNom(parametrage.nom());
        utilisateur.setPrenom(parametrage.prenom());
        utilisateur.setEmail(parametrage.email());
        authentificationService.modifierUnCompte(utilisateur);
        return ResponseEntity.ok().body(new Message("Utilisateur modifié"));
    }
}
