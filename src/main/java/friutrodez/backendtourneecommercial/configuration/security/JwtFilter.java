package friutrodez.backendtourneecommercial.configuration.security;

import friutrodez.backendtourneecommercial.service.AuthenticationService;
import friutrodez.backendtourneecommercial.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @see OncePerRequestFilter
 * Filtre d'authentification pour les tokens
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;

    private final JwtService jwtService;

    /**
     *
     * @param authenticationService Récupération d'un service pour authentifier l'utilisateur.
     *                             Annotée avec lazy car le filtre doit être créé avant le service
     * @param jwtService Le service pour les jwt.
     */
    @Autowired
    public JwtFilter( @Lazy AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    /**
     * Authentifie par rapport au token reçu dans la requête
     * Si le token n'est pas correct, l'utilisateur n'est pas connecté
     *
     * @param request     la requête envoyée au serveur
     * @param response    la réponse à renvoyer
     * @param filterChain La chaine de filtre configurée
     * @throws ServletException en cas d'échec du doFilter
     * @throws IOException      en cas d'échec du doFilter
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader("Authorization");

            // Vérifie si le header possède le bearer token
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // Enlève le "bearer"
            final String jwt = authHeader.substring(7);
            final String username = jwtService.extractEmail(jwt);

            if (username != null && null == SecurityContextHolder.getContext().getAuthentication()) {
                UserDetails userDetails = authenticationService.loadUserDetails(username);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    authenticationService.tryAuthenticateWithRequest(userDetails, request);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            filterChain.doFilter(request, response);
        } 
    }


    /**
     * Crée un token d'authentification pour l'ajout d'une connexion dans SecurityContext
     *
     * @param userDetails les informations de l'utilisateur
     * @param request     la requête envoyée au serveur
     * @return un toke d'authentification
     */
    private UsernamePasswordAuthenticationToken createAuthToken(UserDetails userDetails, HttpServletRequest request) {
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
     * La requête peut être effectuée après cette connexion.
     *
     * @param authToken le token d'authentification
     */
    private void setAuthentication(UsernamePasswordAuthenticationToken authToken) {
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
