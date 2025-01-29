package friutrodez.backendtourneecommercial.controller;

import friutrodez.backendtourneecommercial.dto.JwtToken;
import friutrodez.backendtourneecommercial.service.AuthentificationService;
import friutrodez.backendtourneecommercial.dto.DonneesAuthentification;
import friutrodez.backendtourneecommercial.service.JwtService;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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

    private final AuthentificationService authentificationService;

    /**
     * Constructeur de la classe AuthentificationControlleur
     *
     * @param jwtService Service pour la gestion des tokens JWT
     * @param authentificationService Service d'authentification des utilisateurs
     */
    @Autowired
    public AuthentificationControlleur(JwtService jwtService, AuthentificationService authentificationService) {
        this.jwtService = jwtService;
        this.authentificationService = authentificationService;
    }

    /**
     * Authentifie un utilisateur en fonction de ses données d'authentification.
     * @param donneesAuthentification
     * @return ResponseEntity contenant un token JWT si l'authentification réussit, sinon une réponse d'erreur
     */
    @PostMapping(path = "/authentifier")
    public ResponseEntity<JwtToken> authentifier(@RequestBody DonneesAuthentification donneesAuthentification) {
        Utilisateur utilisateur = authentificationService.authentifier(donneesAuthentification);

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
    public ResponseEntity CreerUnCompte(@RequestBody Utilisateur utilisateur) {
        Utilisateur utilisateurCreer =  authentificationService.creerUnCompte(utilisateur);
        if (utilisateurCreer == null) {
            return ResponseEntity.badRequest().body("Adresse invalide");
        }
        return ResponseEntity.ok(utilisateurCreer);
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
