package friutrodez.backendtourneecommercial.controller;

import friutrodez.backendtourneecommercial.dto.DonneesAuthentification;
import friutrodez.backendtourneecommercial.dto.JwtToken;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.service.AuthenticationService;
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
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
@RequestMapping(path = "/auth/")
@RestController
@Validated
public class AuthenticationController {

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    /**
     * Constructeur de la classe AuthentificationControlleur
     *
     * @param jwtService            Service pour la gestion des tokens JWT
     * @param authenticationService Service d'authentification des utilisateurs
     */
    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    /**
     * Endpoint d'API pour authentifier l'utilisateur avec son email et mot de passe.
     *
     * @param authData Les données d'authentifications.
     * @return Un JWTToken pour permettre à l'utilisateur d'accéder aux autres ressources.
     * Le temps d'expiration est donné aussi.
     */
    @PostMapping
    public ResponseEntity<JwtToken> authenticate(@RequestBody DonneesAuthentification authData) {
        Utilisateur utilisateur = authenticationService.tryAuthenticate(authData);
        String jwtToken = jwtService.generateToken(utilisateur);
        JwtToken jwtTokenDTO = new JwtToken(jwtToken, jwtService.JWT_EXPIRATION);
        return ResponseEntity.ok(jwtTokenDTO);
    }
}
