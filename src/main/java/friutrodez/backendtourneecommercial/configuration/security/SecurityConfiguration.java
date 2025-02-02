package friutrodez.backendtourneecommercial.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Classe de configuration de sécurité permettant de filtrer
 * les requêtes du web qui communique au framework Spring.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
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
     *
     * @param http la configuration de sécurité HTTP
     * @return Une chaine de sécurité qui va filtrer les requêtes non-authentifiées, sauf pour
     * la création de compte et l'authentification.
     * @throws Exception si une erreur survient lors de la désactivation de la protection CSRF
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return // La protection CSRF (Cross Site Request Forgery) est désactivée
                http.csrf(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(
                                // Autorise les requêtes de création d'utilisateur et d'authentification
                                (authorizeHttpRequests) -> {
                                    authorizeHttpRequests.requestMatchers("/auth/").permitAll()
                                            .requestMatchers(HttpMethod.POST, "/utilisateur/").permitAll()
                                            // Toutes les autres requêtes nécessitent une authentification
                                            .anyRequest().authenticated();
                                }

                        ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                        // Spring ne sauvegarde pas les utilisateurs authentifiés
                        .sessionManagement(
                                (httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.
                                        sessionCreationPolicy(SessionCreationPolicy.STATELESS)))
                        .authenticationProvider(authenticationProvider).build();
    }
}