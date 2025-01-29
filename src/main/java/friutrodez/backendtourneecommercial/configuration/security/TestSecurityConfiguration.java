package friutrodez.backendtourneecommercial.configuration.security;

import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mysql.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Profile("test")
@Configuration
public class TestSecurityConfiguration {

    @Bean
    protected SecurityFilterChain testConfiguration(HttpSecurity http) throws Exception {
        //TODO add default authenticated user
        return http.csrf(customize -> customize.disable())
                .authorizeHttpRequests(customer -> customer.anyRequest().authenticated()).build();
    }
}