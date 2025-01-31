package friutrodez.backendtourneecommercial.controller;

import friutrodez.backendtourneecommercial.dto.JwtToken;
import friutrodez.backendtourneecommercial.service.AuthenticationService;
import friutrodez.backendtourneecommercial.dto.DonneesAuthentification;
import friutrodez.backendtourneecommercial.service.JwtService;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * Controlleur pour gérer les requêtes gérant l'authentification et la gestion de compte
 *
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
@RequestMapping(path = "/auth")
@RestController
@Validated
public class AuthentificationControlleur {

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    /**
     * Constructeur de la classe AuthentificationControlleur
     *
     * @param jwtService Service pour la gestion des tokens JWT
     * @param authenticationService Service d'authentification des utilisateurs
     */
    @Autowired
    public AuthentificationControlleur(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "/authentifier")
    public ResponseEntity<JwtToken> authentifier(@RequestBody DonneesAuthentification donneesAuthentification) {
        Utilisateur utilisateur = authenticationService.tryAuthenticate(donneesAuthentification);

        String jwtToken = jwtService.genererToken(utilisateur);
        JwtToken jwtTokenDTO = new JwtToken(jwtToken,jwtService.JWT_EXPIRATION);
        return  ResponseEntity.ok(jwtTokenDTO);

    }

    /**
     * Crée un compte utilisateur.
     *
     * @param utilisateur Objet contenant les informations du nouvel utilisateur
     * @return ResponseEntity contenant l'utilisateur créé ou un message d'erreur si la création échoue
     */
    @PostMapping(path = "/creer")
    public ResponseEntity<Map<String,String>> CreerUnCompte(@RequestBody Utilisateur utilisateur) {
        Utilisateur utilisateurCreer =  authenticationService.createAnAccount(utilisateur);
        if (utilisateurCreer == null) {
            return ResponseEntity.badRequest().body(Map.of("message","Adresse invalide"));
        }
        return ResponseEntity.ok(Map.of("message","L'utilisateur a été créé"));
    }

    /**
     * STUB
     * méthode de test pour tester le token après authentification
     * @return
     */
    @GetMapping
    public String testToken() {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "token fonctionnel";
    }

}
