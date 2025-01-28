package friutrodez.backendtourneecommercial.controller;

import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mysql.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping(path = "/utilisateur/")
@RestController
public class UtilisateurControlleur {

    @Autowired
    UtilisateurRepository utilisateurRepository;

    @PostMapping(path = "modifier")
    public ResponseEntity<Map<String,String>> modifierUtilisateur(@RequestBody Utilisateur utilisateurModif) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        utilisateurModif.setId(utilisateur.getId());
        utilisateurRepository.save(utilisateurModif);
        return ResponseEntity.ok().body(Map.of("messaee","L'utilisateur a été modifié"));
    }

    @DeleteMapping(path = "supprimer")
    public  ResponseEntity<Map<String,String>> supprimerUtilisateur() {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        utilisateurRepository.deleteById(utilisateur.getId());
        return ResponseEntity.ok().body(Map.of("message","L'utilisateur a été supprimé."));
    }
}
