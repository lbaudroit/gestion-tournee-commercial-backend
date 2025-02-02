package friutrodez.backendtourneecommercial.controller;

import friutrodez.backendtourneecommercial.dto.Message;
import friutrodez.backendtourneecommercial.dto.Parametrage;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
@AllArgsConstructor
@RequestMapping(path = "/utilisateur/")
@RestController
@Validated
public class UtilisateurController {

    private AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<Parametrage> getUtilisateur() {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Parametrage parametrage = new Parametrage(user.getNom(), user.getPrenom(), user.getEmail());
        return ResponseEntity.ok().body(parametrage);

    }

    /**
     * Crée un compte utilisateur.
     *
     * @param utilisateur Objet contenant les informations du nouvel utilisateur
     * @return ResponseEntity contenant l'utilisateur créé ou un message d'erreur si la création échoue
     */
    @PostMapping
    public ResponseEntity<Message> createUtilisateur(@RequestBody Utilisateur utilisateur) {
        Utilisateur createdUser = authenticationService.createAnAccount(utilisateur);

        if (createdUser == null) {
            return ResponseEntity.badRequest().body(new Message("Adresse invalide"));
        }
        return ResponseEntity.ok(new Message("L'utilisateur a été créé"));
    }

    @PutMapping
    public ResponseEntity<Message> modifyUtilisateur(@RequestBody Parametrage parametrage) {
        Utilisateur user = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        user.setNom(parametrage.nom());
        user.setPrenom(parametrage.prenom());
        user.setEmail(parametrage.email());
        try {
            authenticationService.editAnAccount(user);
            return ResponseEntity.ok().body(new Message("Utilisateur modifié"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
