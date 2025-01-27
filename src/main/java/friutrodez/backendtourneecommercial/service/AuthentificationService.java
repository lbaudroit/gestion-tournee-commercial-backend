package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.dto.DonneesAuthentification;
import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.exception.DonneesManquantesException;
import friutrodez.backendtourneecommercial.exception.AdresseInvalideException;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mysql.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Classe métier qui gére l'authenthication pour les utilisateurs
 *
 */
@Service
public class AuthentificationService {

    private final UtilisateurRepository utilisateurRepository;

    private final PasswordEncoder encodeurDeMotDePasse;
    private final AuthenticationManager authenticationManager;
    private final AdresseToolsService addressToolsService = new AdresseToolsService();

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
    public Utilisateur creerUnCompte(Utilisateur utilisateur) throws DonneesInvalidesException,DonneesManquantesException{
        if(utilisateur.getEmail() == null || utilisateur.getEmail().trim().isEmpty()) {
            throw new DonneesManquantesException("L'utilisateur n'a pas d'email défini.");
        }
        if(!utilisateur.getEmail().matches("^[^@]+@[^@]+\\.[^@]+$")) {
            throw new DonneesInvalidesException("L'email de l'utilisateur n'a pas le bon format.");
        }
        utilisateur.setMotDePasse(encodeurDeMotDePasse.encode(utilisateur.getMotDePasse()));
        if (!addressToolsService.validateAdresse(utilisateur.getLibelleAdresse(), utilisateur.getCodePostal(), utilisateur.getVille())) {
            throw new AdresseInvalideException("Adresse invalide");
        }
        else {
            Double[] coordinates = addressToolsService.geolocateAdresse(utilisateur.getLibelleAdresse(), utilisateur.getCodePostal(), utilisateur.getVille());
            utilisateur.setLatitude(coordinates[1]);
            utilisateur.setLongitude(coordinates[0]);
            return utilisateurRepository.save(utilisateur);
        }
    }

    public Utilisateur modifierUnCompte(Utilisateur utilisateur) throws DonneesInvalidesException,DonneesManquantesException{
        if(utilisateur.getEmail() == null || utilisateur.getEmail().trim().isEmpty()) {
            throw new DonneesManquantesException("L'utilisateur n'a pas d'email défini.");
        }
        if(!utilisateur.getEmail().matches("^[^@]+@[^@]+\\.[^@]+$")) {
            throw new DonneesInvalidesException("L'email de l'utilisateur n'a pas le bon format.");
        }
        if (!addressToolsService.validateAdresse(utilisateur.getLibelleAdresse(), utilisateur.getCodePostal(), utilisateur.getVille())) {
            throw new AdresseInvalideException("Adresse invalide");
        }
        else {
            Double[] coordinates = addressToolsService.geolocateAdresse(utilisateur.getLibelleAdresse(), utilisateur.getCodePostal(), utilisateur.getVille());
            utilisateur.setLatitude(coordinates[1]);
            utilisateur.setLongitude(coordinates[0]);
            return utilisateurRepository.save(utilisateur);
        }
    }
}
