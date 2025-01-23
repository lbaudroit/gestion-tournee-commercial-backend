package friutrodez.backendtourneecommercial.controller;

import friutrodez.backendtourneecommercial.dto.JwtToken;
import friutrodez.backendtourneecommercial.service.AuthentificationService;
import friutrodez.backendtourneecommercial.dto.DonneesAuthentification;
import friutrodez.backendtourneecommercial.service.JwtService;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import java.security.Principal;


/**
 * Controlleur pour gérer les requêtes gérant l'authentification et la gestion de compte
 */
@RequestMapping(path = "/auth")
@RestController
public class AuthentificationController {

    private final JwtService jwtService;

    private final AuthentificationService authentificationService;

    @Autowired
    public AuthentificationController(JwtService jwtService, AuthentificationService authentificationService) {
        this.jwtService = jwtService;
        this.authentificationService = authentificationService;
    }

    @PostMapping(path = "/authentifier")
    public ResponseEntity<JwtToken> authentifier(@RequestBody DonneesAuthentification donneesAuthentification) {
        Utilisateur utilisateur = authentificationService.authentifier(donneesAuthentification);

        String jwtToken = jwtService.genererToken(utilisateur);
        JwtToken jwtTokenDTO = new JwtToken(jwtToken,jwtService.JWT_EXPIRATION);
        return  ResponseEntity.ok(jwtTokenDTO);

    }

    @PostMapping(path = "/creer")
    public ResponseEntity<Utilisateur> CreerUnCompte(@RequestBody Utilisateur utilisateur) {
       Utilisateur utilisateurCreer =  authentificationService.creerUnCompte(utilisateur);

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
