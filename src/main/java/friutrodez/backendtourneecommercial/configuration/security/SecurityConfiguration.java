package friutrodez.backendtourneecommercial.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
@Profile("production")
public class SecurityConfiguration {

    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    AuthenticationProvider authenticationProvider;

    /**
     * Filtre de l'api pour la sécurisée
     * @param http La requête
     * @return Une chaine de sécurité qui va déterminer la façon dont les requêtes vont être filtrées
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return // Le csrf (Cross Site Request Forgery) est désactivé
                // Elle n'est pas utile
                http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .authorizeHttpRequests(
                        // Autorise les requêtes de /auth/* et /hello
                (authorizeHttpRequests)->authorizeHttpRequests.requestMatchers("/auth/*","/hello")
                        .permitAll()
                        // Toutes les autres requêtes nécessitent une authentification
                       .anyRequest().authenticated()

        ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                        // Spring ne sauvegarde pas les utilisateurs authentifiées
                .sessionManagement(
                        (httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.
                        sessionCreationPolicy(SessionCreationPolicy.STATELESS)))
                .authenticationProvider(authenticationProvider).build();
    }
}