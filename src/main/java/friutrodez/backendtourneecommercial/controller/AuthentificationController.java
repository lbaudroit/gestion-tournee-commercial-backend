package friutrodez.backendtourneecommercial.controller;

import friutrodez.backendtourneecommercial.dto.JwtToken;
import friutrodez.backendtourneecommercial.service.AuthentificationService;
import friutrodez.backendtourneecommercial.dto.AuthentificationUtilisateur;
import friutrodez.backendtourneecommercial.service.JwtService;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<JwtToken> authentifier(@RequestBody AuthentificationUtilisateur authentificationUtilisateur) {
        Utilisateur utilisateur = authentificationService.authentifier(authentificationUtilisateur);
        String jwtToken = jwtService.generateToken(utilisateur);
        JwtToken jwtTokenDTO = new JwtToken(jwtToken,jwtService.JWT_EXPIRATION);
        return  ResponseEntity.ok(jwtTokenDTO);

    }

    @PostMapping(path = "/creer")
    public ResponseEntity<Utilisateur> CreerUnCompte(@RequestBody Utilisateur utilisateur) {
       Utilisateur utilisateurCreer =  authentificationService.CreerUnCompte(utilisateur);

       return ResponseEntity.ok(utilisateurCreer);
    }
    @GetMapping
    public String testToken() {
        return "token fonctionnel";
    }

}
