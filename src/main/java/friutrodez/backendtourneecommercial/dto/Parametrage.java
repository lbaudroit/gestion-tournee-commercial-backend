package friutrodez.backendtourneecommercial.dto;

/**
 * Paramétrage envoyé au client de l'API
 *
 * @param nom    nom de l'utilisateur
 * @param prenom prénom de l'utilisateur
 * @param email  email de l'utilisateur
 */
public record Parametrage(String nom, String prenom, String email) {
}
