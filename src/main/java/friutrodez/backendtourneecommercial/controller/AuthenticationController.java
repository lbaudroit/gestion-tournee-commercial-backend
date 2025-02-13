package friutrodez.backendtourneecommercial.controller;

import friutrodez.backendtourneecommercial.dto.DonneesAuthentification;
import friutrodez.backendtourneecommercial.dto.JwtToken;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.service.UtilisateurService;
import friutrodez.backendtourneecommercial.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Contrôleur pour gérer les requêtes d'authentification et de gestion de compte
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
@RequestMapping(path = "/auth/")
@RestController
@Validated
public class AuthenticationController {

    private final JwtService jwtService;

    private final UtilisateurService authenticationService;

    /**
     * Constructeur de la classe AuthentificationControlleur
     *
     * @param jwtService            Service pour la gestion des tokens JWT
     * @param authenticationService Service d'authentification des utilisateurs
     */
    public AuthenticationController(JwtService jwtService, UtilisateurService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<JwtToken> authenticate(@RequestBody DonneesAuthentification authData) {
        Utilisateur utilisateur = authenticationService.tryAuthenticate(authData);

        String jwtToken = jwtService.generateToken(utilisateur);
        JwtToken jwtTokenDTO = new JwtToken(jwtToken, jwtService.JWT_EXPIRATION);
        return ResponseEntity.ok(jwtTokenDTO);
    }
}
