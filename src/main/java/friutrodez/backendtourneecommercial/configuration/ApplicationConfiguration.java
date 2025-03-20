package friutrodez.backendtourneecommercial.configuration;

import friutrodez.backendtourneecommercial.repository.mysql.UtilisateurRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Classe de configuration de l'application.
 * Met à disposition les outils d'authentification
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
@Configuration
public class ApplicationConfiguration {

    final UtilisateurRepository utilisateurRepository;

    /**
     * Constructeur de la classe ApplicationConfiguration
     *
     * @param utilisateurRepository Le repository des utilisateurs
     */
    @Autowired
    public ApplicationConfiguration(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Récupère un utilisateur par son email
     *
     * @return Un utilisateur par son email
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> utilisateurRepository.findByEmail(username);

    }

    /**
     * Récupère un gestionnaire d'authentification
     *
     * @param config La configuration de l'authentification
     * @return Un gestionnaire d'authentification
     * @throws Exception si une erreur survient lors de la récupération du gestionnaire d'authentification
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Récupère un encodeur de mot de passe
     *
     * @return Un encodeur de mot de passe
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Récupère un fournisseur d'authentification
     *
     * @return Un fournisseur d'authentification
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}
