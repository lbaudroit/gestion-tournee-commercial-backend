package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.dto.AuthentificationUtilisateur;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mysql.UtilisateurRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthentificationService {

    private final UtilisateurRepository utilisateurRepository;

    private final PasswordEncoder encodeurDeMotDePasse;
    private final AuthenticationManager authenticationManager;

    public AuthentificationService(UtilisateurRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.utilisateurRepository = userRepository;
        this.encodeurDeMotDePasse = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public Utilisateur authentifier(AuthentificationUtilisateur input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.nom(),
                        input.motDePasse()
                )
        );
        return utilisateurRepository.findByNom(input.nom());
    }
    public Utilisateur CreerUnCompte(Utilisateur input) {
        input.setMotDePasse(encodeurDeMotDePasse.encode(input.getMotDePasse()));
        return utilisateurRepository.save(input);
    }
}
