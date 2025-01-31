package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.dto.DonneesAuthentification;
import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.exception.DonneesManquantesException;
import friutrodez.backendtourneecommercial.exception.AdresseInvalideException;
import friutrodez.backendtourneecommercial.model.Adresse;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mysql.UtilisateurRepository;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

/**
 * Service de gestion de l'authentification.
 *
 * Cette classe fournit des méthodes pour authentifier ou créer le compte d'un utilisateur.
 *
 * @author
 * Benjamin NICOL
 * Enzo CLUZEL
 * Leïla BAUDROIT
 * Ahmed BRIBACH
 */
@Service
public class AuthenticationService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AdresseToolsService addressToolsService = new AdresseToolsService();
    private final ValidatorService validatorService ;

    /**
     * @param userRepository Un repository pour l'utilisateur.
     * @param passwordEncoder Un encodeur de mot de posse.
     * @param authenticationManager Un manageur pour authentifier l'utilisateur.
     * @param validatorService Un service pour valider la ressource.
     */
    @Autowired
    public AuthenticationService(UtilisateurRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, ValidatorService validatorService) {
        this.utilisateurRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.validatorService = validatorService;
    }

    /**
     * Méthode pour authentifier un utilisateur dans l' "authenticationManager".
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
     * @param user L'utilisateur à enregistrer en base de donnée.
     * @return l'utilisateur avec le mot de passe encrypté.
     */
    public Utilisateur createAnAccount(Utilisateur user) {
        validatorService.mustValidate(user);
        checkAddress(new Adresse(user.getLibelleAdresse(),user.getCodePostal(), user.getVille()));

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
     * @param editData Les modifications apportées à l'utilisateur.
     * @return L'utilisateur modifiée.
     */
    public Utilisateur editAnAccount(Utilisateur editData) {
        validatorService.mustValidate(editData);
        Utilisateur savedUser = utilisateurRepository.findById(editData.getId()).get();

        String encodedPasswordUser = passwordEncoder.encode(editData.getPassword());

        if(!encodedPasswordUser.equals(savedUser.getPassword())) {
            editData.setMotDePasse(encodedPasswordUser);
        }

        checkAddress(new Adresse(editData.getLibelleAdresse(),editData.getCodePostal(),editData.getVille()));

        Double[] coordinates = addressToolsService.geolocateAdresse(editData.getLibelleAdresse(), editData.getCodePostal(), editData.getVille());
        editData.setLatitude(coordinates[1]);
        editData.setLongitude(coordinates[0]);
        return utilisateurRepository.save(editData);

    }


    /**
     * Méthode de vérification de l'email
     * @param adress L'adresse à vérifier
     */
    public  void checkAddress(Adresse adress) {
        if(!addressToolsService.validateAdresse(adress.getLibelle(),
                adress.getCodePostal(), adress.getVille())) {
            throw new AdresseInvalideException("Adresse invalide");
        }

    }
}
