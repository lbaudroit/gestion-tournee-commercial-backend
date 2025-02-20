package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.dto.DonneesAuthentification;
import friutrodez.backendtourneecommercial.exception.AdresseInvalideException;
import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.model.Adresse;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mysql.UtilisateurRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

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
public class AuthenticationService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AdresseToolsService addressToolsService = new AdresseToolsService();
    private final ValidatorService validatorService;
    private final UserDetailsService userDetailsService;

    /**
     * @param userRepository        Un repository pour l'utilisateur.
     * @param passwordEncoder       Un encodeur de mot de posse.
     * @param authenticationManager Un manageur pour authentifier l'utilisateur.
     * @param validatorService      Un service pour valider la ressource.
     */
    public AuthenticationService(UtilisateurRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, ValidatorService validatorService, UserDetailsService userDetailsService) {
        this.utilisateurRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.validatorService = validatorService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Méthode pour authentifier un utilisateur dans l' "authenticationManager".
     *
     * @param donneeAuthentification Les données pour l'authentification.
     * @return l'utilisateur authentifié.
     */
    public Utilisateur tryAuthenticate(DonneesAuthentification donneeAuthentification) {
        Authentication authentication =authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        donneeAuthentification.email(),
                        donneeAuthentification.motDePasse()
                )

        );
        setAuthentication(authentication);
        return (Utilisateur) authentication.getPrincipal();
    }

    public UserDetails loadUserDetails(String username) {
        return userDetailsService.loadUserByUsername(username);
    }

    /**
     * Méthode pour authentifier un utilisateur dans l' "authenticationManager". Ajoute les informations de la requête en cours.
     * @param userDetails Les détails utilisateur.
     * @param request La requête en cours.
     * @return L'utilisateur authentifié.
     */
    public Utilisateur tryAuthenticateWithRequest(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,null,userDetails.getAuthorities()
        );

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        setAuthentication(authToken);
        return (Utilisateur) authToken.getPrincipal();
    }

    /**
     * Connecte l'utilisateur à Spring en l'ajoutant au SecurityContext.<br>
     * La requête peut être effectuée après cette connexion.
     *
     * @param authentication l'authentification à ajouter dans le contexte de la sécurité.
     */
    private void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
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
     * Le mot de passe sera encryptée.
     * Des vérifications métiers sont effectuées.
     *
     * @param editData Les modifications apportées à l'utilisateur.
     * @return L'utilisateur modifié.
     */
    public Utilisateur editAnAccount(Utilisateur editData) {
        validatorService.mustValidate(editData);
        Optional<Utilisateur> user = utilisateurRepository.findById(editData.getId());
        Utilisateur savedUser;
        if (user.isPresent()) {
            savedUser = user.get();
        } else {
            throw new NoSuchElementException("L'utilisateur n'existe pas.");
        }

        String encodedPasswordUser = passwordEncoder.encode(editData.getPassword());

        if (!encodedPasswordUser.equals(savedUser.getPassword())) {
            editData.setMotDePasse(encodedPasswordUser);
        }

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
