package friutrodez.backendtourneecommercial.controller;

import friutrodez.backendtourneecommercial.dto.Message;
import friutrodez.backendtourneecommercial.dto.Parametrage;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mysql.UtilisateurRepository;
import friutrodez.backendtourneecommercial.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@Validated
public class UtilisateurControlleur {

    @Autowired
    UtilisateurRepository utilisateurRepository;
    @Autowired
    private AuthenticationService authenticationService;

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
    public ResponseEntity<Message> CreerUnCompte(@RequestBody Utilisateur utilisateur) {
        Utilisateur utilisateurCreer = authenticationService.createAnAccount(utilisateur);
        if (utilisateurCreer == null) {
            return ResponseEntity.badRequest().body(new Message("Adresse invalide"));
        }
        return ResponseEntity.ok(new Message("L'utilisateur a été créé"));
    }

    @PutMapping
    public ResponseEntity<Message> modifierUtilisateur(@RequestBody Parametrage parametrage) {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        utilisateur.setNom(parametrage.nom());
        utilisateur.setPrenom(parametrage.prenom());
        utilisateur.setEmail(parametrage.email());
        authenticationService.editAnAccount(utilisateur);
        return ResponseEntity.ok().body(new Message("Utilisateur modifié"));
    }
}
