package friutrodez.backendtourneecommercial.dto;

/**
 * Token envoyé au client de l'API qui se connecte
 *
 * @param token
 * @param expiration
 */
public record JwtToken(String token, long expiration) {
}
