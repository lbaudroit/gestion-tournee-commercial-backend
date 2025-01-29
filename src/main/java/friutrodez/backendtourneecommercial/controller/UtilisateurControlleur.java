package friutrodez.backendtourneecommercial.controller;

import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mysql.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Modifie les informations de l'utilisateur authentifié.
     *
     * @param utilisateurModif Objet contenant les nouvelles informations de l'utilisateur.
     * @return ResponseEntity contenant l'utilisateur modifié ou un message d'erreur en cas d'échec.
     * @throws IllegalArgumentException si l'objet utilisateurModif est null.
     */
    @PostMapping(path = "modifier")
    public ResponseEntity<Map<String,String>> modifierUtilisateur(@RequestBody Utilisateur utilisateurModif) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        utilisateurModif.setId(utilisateur.getId());
        utilisateurRepository.save(utilisateurModif);
        return ResponseEntity.ok().body(Map.of("messaee","L'utilisateur a été modifié"));
    }

    /**
     * Supprime le compte de l'utilisateur authentifié.
     *
     * @return ResponseEntity avec un message de confirmation ou une erreur si l'opération échoue.
     */
    @DeleteMapping(path = "supprimer")
    public  ResponseEntity<Map<String,String>> supprimerUtilisateur() {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        utilisateurRepository.deleteById(utilisateur.getId());
        return ResponseEntity.ok().body(Map.of("message","L'utilisateur a été supprimé."));
    }
}
