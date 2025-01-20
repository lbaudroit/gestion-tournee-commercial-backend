package friutrodez.backendtourneecommercial.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Profile("test")
@Configuration
public class TestSecurityConfiguration {

    @Bean
    protected SecurityFilterChain testConfiguration(HttpSecurity http) throws Exception {
        return http.csrf(customize-> customize.disable())
                .authorizeHttpRequests(customer -> customer.anyRequest().permitAll()).build();
    }
}