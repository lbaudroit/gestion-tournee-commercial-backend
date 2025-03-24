package friutrodez.backendtourneecommercial.configuration.security;

import friutrodez.backendtourneecommercial.service.AuthenticationService;
import friutrodez.backendtourneecommercial.service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Filtre d'authentification pour les tokens
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 * @see OncePerRequestFilter
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;

    private final JwtService jwtService;

    /**
     * @param authenticationService Récupération d'un service pour authentifier l'utilisateur.
     *                              Annotée avec lazy car le filtre doit être créé avant le service
     * @param jwtService            Le service pour les jwt.
     */
    @Autowired
    public JwtFilter(@Lazy AuthenticationService authenticationService, JwtService jwtService) {
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
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
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
        } catch (ExpiredJwtException ex) {
            filterChain.doFilter(request, response);
        }
    }
}
