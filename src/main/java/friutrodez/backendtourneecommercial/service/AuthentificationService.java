package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.dto.DonneesAuthentification;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mysql.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthentificationService {

    private final UtilisateurRepository utilisateurRepository;

    private final PasswordEncoder encodeurDeMotDePasse;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthentificationService(UtilisateurRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.utilisateurRepository = userRepository;
        this.encodeurDeMotDePasse = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Utilisé cette méthode pour authentifier un utilisateur
     * @param donneeAuthentification Les données pour authentification
     * @return l'utilisateur authentifié
     */
    public Utilisateur authentifier(DonneesAuthentification donneeAuthentification) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        donneeAuthentification.email(),
                        donneeAuthentification.motDePasse()
                )
        );
        return utilisateurRepository.findByEmail(donneeAuthentification.email());
    }

    /**
     * Creation de l'utilisateur dans la base de donnée avec le mot de passe encryptée
     * Utilisé cette méthode pour sécuriser les utilisateurs
     * @param utilisateur à enregistrer en base de donnée
     * @return l'utilisateur avec le mot de passe encrypté
     */
    public Utilisateur creerUnCompte(Utilisateur utilisateur) {
        utilisateur.setMotDePasse(encodeurDeMotDePasse.encode(utilisateur.getMotDePasse()));
        return utilisateurRepository.save(utilisateur);
    }
}
