package friutrodez.backendtourneecommercial.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Classe de configuration de sécurité permettant de filtrer
 * les requêtes du web qui communique au framework Spring.
 *
 * @author
 * Benjamin NICOL
 * Enzo CLUZEL
 * Leïla BAUDROIT
 * Ahmed BRIBACH
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    /**
     * Une chaîne de sécurité qui ne filtre actuellement aucune requête.
     * Permet d'éviter de devoir se connecter.
     *
     * @param http La requête HTTP.
     * @return Une chaîne de sécurité qui va déterminer si la requête est acceptée.
     * @throws Exception En cas d'erreur de configuration de la sécurité.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                (authorizeHttpRequests) -> authorizeHttpRequests
                        .anyRequest()
                        .permitAll()
        ).build();
    }
}