package friutrodez.backendtourneecommercial.configuration.security;

import friutrodez.backendtourneecommercial.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * doc : org.springframework.web.filter.OncePerRequestFilter
 * Filtre d'authentification pour les tokens
 */
@Component
public class JwtFilter extends OncePerRequestFilter {


    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtFilter(UserDetailsService userDetailsService, JwtService jwtService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Authentifie par rapport au token reçu dans la requête
     * Si le token n'est pas correcte l'utilisateur n'est pas connecté
     * @param request
     * @param response
     * @param filterChain La chaine de filtre
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // Vérifie si le header possède le bearer token
        if(authHeader ==null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }
        final String jwt = authHeader.substring(7);
        final  String username = jwtService.extraireNomUtilisateur(jwt);
        if(username!= null && null ==SecurityContextHolder.getContext().getAuthentication() ) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if(jwtService.tokenEstValide(jwt,userDetails)) {
                setAuthentification(creerAuthToken(userDetails,request));
            }
        }
        filterChain.doFilter(request,response);

    }


    /**
     * Crée un token d'authentification pour l'ajout d'une connexion dans SecurityContext
     * @param userDetails
     * @param request
     * @return un toke d'authentification
     */
    private UsernamePasswordAuthenticationToken creerAuthToken(UserDetails userDetails,HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authToken;
    }

    /**
     * Connecte l'utilisateur à Spring en l'ajoutant au SecurityContext.<br>
     * La requête peut être effectuée après cette connexion
     * @param authToken
     */
    private void setAuthentification(UsernamePasswordAuthenticationToken authToken) {
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
