package friutrodez.backendtourneecommercial.dto;

/**
 * Token envoyée au client de l'API qui se connecte
 * @param token
 * @param expiration
 */
public record JwtToken(String token,long expiration) {
}
