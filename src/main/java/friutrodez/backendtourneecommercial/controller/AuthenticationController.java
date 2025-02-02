package friutrodez.backendtourneecommercial.controller;

import friutrodez.backendtourneecommercial.dto.JwtToken;
import friutrodez.backendtourneecommercial.dto.DonneesAuthentification;
import friutrodez.backendtourneecommercial.service.AuthenticationService;
import friutrodez.backendtourneecommercial.service.JwtService;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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

    private final AuthenticationService authenticationService;

    /**
     * Constructeur de la classe AuthentificationControlleur
     *
     * @param jwtService Service pour la gestion des tokens JWT
     * @param authenticationService Service d'authentification des utilisateurs
     */
    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<JwtToken> authenticate(@RequestBody DonneesAuthentification authData) {
        Utilisateur utilisateur = authenticationService.tryAuthenticate(authData);

        String jwtToken = jwtService.generateToken(utilisateur);
        JwtToken jwtTokenDTO = new JwtToken(jwtToken,jwtService.JWT_EXPIRATION);
        return  ResponseEntity.ok(jwtTokenDTO);
    }
}
