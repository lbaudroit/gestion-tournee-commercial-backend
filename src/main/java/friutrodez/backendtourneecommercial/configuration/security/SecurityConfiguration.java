package friutrodez.backendtourneecommercial.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Classe de configuration de sécurité permettant de filtrer
 * les requêtes du web qui communique au framework Spring
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    AuthenticationProvider authenticationProvider;

    /**
     * Une chaine de securité qui ne filtre actuellement aucune requête.
     * Permet d'éviter de devoir se connecter
     * @param http La requête
     * @return Une chaine de sécurité qui va déterminer si la requête est acceptée
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return  http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable()).authorizeHttpRequests(
                (authorizeHttpRequests)->authorizeHttpRequests.requestMatchers("/auth/*")
                        .permitAll().requestMatchers("/hello").permitAll().anyRequest().authenticated()

        ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(
                        (httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.
                        sessionCreationPolicy(SessionCreationPolicy.STATELESS)))
                .authenticationProvider(authenticationProvider).build();
    }
}
