package friutrodez.backendtourneecommercial.dto;

/**
 * Les données d'authentification pour la connexion à l'API
 * Utilisé lors de l'authentification
 *
 * @param email      email de l'utilisateur
 * @param motDePasse mot de passe de l'utilisateur
 */
public record DonneesAuthentification(String email, String motDePasse) {

}
