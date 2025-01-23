package friutrodez.backendtourneecommercial.controller;

import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mysql.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/utilisateur/")
@RestController
public class UtilisateurControlleur {

    @Autowired
    UtilisateurRepository utilisateurRepository;

    @PostMapping(path = "modifier")
    public ResponseEntity<Utilisateur> modifierUtilisateur(@RequestBody Utilisateur utilisateurModif) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        utilisateurModif.setId(utilisateur.getId());
        return ResponseEntity.ok().body(utilisateurRepository.save(utilisateurModif));
    }

    @DeleteMapping(path = "supprimer")
    public  ResponseEntity<String> supprimerUtilisateur() {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        utilisateurRepository.deleteById(utilisateur.getId());
        return ResponseEntity.ok().body("L'utilisateur a été supprimé.");
    }
}
