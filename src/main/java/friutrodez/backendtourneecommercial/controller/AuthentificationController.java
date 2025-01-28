package friutrodez.backendtourneecommercial.controller;

import friutrodez.backendtourneecommercial.dto.JwtToken;
import friutrodez.backendtourneecommercial.service.AuthentificationService;
import friutrodez.backendtourneecommercial.dto.DonneesAuthentification;
import friutrodez.backendtourneecommercial.service.JwtService;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import java.security.Principal;
import java.util.Map;


/**
 * Controlleur pour gérer les requêtes gérant l'authentification et la gestion de compte
 */
@RequestMapping(path = "/auth")
@RestController
@Validated
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
    public ResponseEntity<Map<String,String>> CreerUnCompte(@RequestBody Utilisateur utilisateur) {
        Utilisateur utilisateurCreer =  authentificationService.creerUnCompte(utilisateur);
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
