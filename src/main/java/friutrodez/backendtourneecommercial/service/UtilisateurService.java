package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.dto.DonneesAuthentification;
import friutrodez.backendtourneecommercial.exception.AdresseInvalideException;
import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.model.Adresse;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mysql.UtilisateurRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * Service de gestion de l'authentification.
 * <p>
 * Cette classe fournit des méthodes pour authentifier ou créer le compte d'un utilisateur.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AdresseToolsService addressToolsService = new AdresseToolsService();
    private final ValidatorService validatorService;

    /**
     * @param userRepository        Un repository pour l'utilisateur.
     * @param passwordEncoder       Un encodeur de mot de posse.
     * @param authenticationManager Un manageur pour authentifier l'utilisateur.
     * @param validatorService      Un service pour valider la ressource.
     */
    public UtilisateurService(UtilisateurRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, ValidatorService validatorService) {
        this.utilisateurRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.validatorService = validatorService;
    }

    /**
     * Méthode pour authentifier un utilisateur dans l' "authenticationManager".
     *
     * @param donneeAuthentification Les données pour l'authentification.
     * @return l'utilisateur authentifié.
     */
    public Utilisateur tryAuthenticate(DonneesAuthentification donneeAuthentification) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        donneeAuthentification.email(),
                        donneeAuthentification.motDePasse()
                )
        );
        return utilisateurRepository.findByEmail(donneeAuthentification.email());
    }

    /**
     * Méthode pour créer un utilisateur dans la BD.
     * Le mot de passe sera encryptée.
     * Des vérifications métiers sont effectuées.
     *
     * @param user L'utilisateur à enregistrer en base de donnée.
     * @return l'utilisateur avec le mot de passe encrypté.
     */
    public Utilisateur createAnAccount(Utilisateur user) {
        validatorService.mustValidate(user);
        checkAddress(new Adresse(user.getLibelleAdresse(), user.getCodePostal(), user.getVille()));

        user.setMotDePasse(passwordEncoder.encode(user.getMotDePasse()));
        Double[] coordinates = addressToolsService.geolocateAdresse(user.getLibelleAdresse(), user.getCodePostal(), user.getVille());
        user.setLatitude(coordinates[1]);
        user.setLongitude(coordinates[0]);
        return utilisateurRepository.save(user);

    }

    /**
     * Méthode pour modifier un utilisateur de la BD.
     * Des vérifications métiers sont effectuées.
     *
     * @param editData Les modifications apportées à l'utilisateur.
     * @return L'utilisateur modifié.
     */
    public Utilisateur editAnAccount(Utilisateur editData) {
        validatorService.mustValidate(editData);
        Utilisateur savedUser = utilisateurRepository.findById(editData.getId()).get();

        checkAddress(new Adresse(editData.getLibelleAdresse(), editData.getCodePostal(), editData.getVille()));

        Double[] coordinates = addressToolsService.geolocateAdresse(editData.getLibelleAdresse(), editData.getCodePostal(), editData.getVille());
        editData.setLatitude(coordinates[1]);
        editData.setLongitude(coordinates[0]);
        return utilisateurRepository.save(editData);

    }

    /**
     * Méthode pour modifier le mot de passe d'un utilisateur
     * Le mot de passe sera encrypté.
     *
     * @param password Les modifications apportées à l'utilisateur.
     * @return L'utilisateur modifié.
     */
    public Utilisateur editPassword(Utilisateur user, String password) {

        Utilisateur savedUser = utilisateurRepository.findById(user.getId())
                .orElseThrow(NoSuchElementException::new);

        checkPassword(password);
        String encodedPasswordUser = passwordEncoder.encode(password);
        savedUser.setMotDePasse(encodedPasswordUser);

        return utilisateurRepository.save(savedUser);
    }

    /**
     * Méthode de vérification de l'adresse postale
     *
     * @param adress L'adresse à vérifier
     */
    public void checkAddress(Adresse adress) {
        if (!addressToolsService.validateAdresse(adress.getLibelle(),
                adress.getCodePostal(), adress.getVille())) {
            throw new AdresseInvalideException("Adresse invalide");
        }
    }

    /**
     * Méthode de vérification du mot de passe
     *
     * @param password le mot de passe, non-encodé, à vérifier
     */
    public void checkPassword(String password) {
        if (!password.matches(Utilisateur.PASSWORD_PATTERN)) {
            throw new DonneesInvalidesException("Le mot de passe est invalide.");
        }
    }
}
