package friutrodez.backendtourneecommercial.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


/**
 * Classe représentant un utilisateur.
 * Cette classe est utilisée pour mapper la table `Utilisateur` dans la base de données.
 * Utilise Lombok pour générer les constructeurs, getters, setters et le builder.
 * <p>
 * L'utilisateur doit avoir :
 * - Un email avec un format correcte
 * - Un mot de passe contenant au moins :
 * 1 majuscule, 1 miniscule, 1 chiffre, 1 caractère spécial, 8 caractères
 * - Une adresse valide en france
 *
 * @author Benjamin NICOL
 * Enzo CLUZEL
 * Leïla BAUDROIT
 * Ahmed BRIBACH
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(
        uniqueConstraints = @UniqueConstraint(name = "unique_email", columnNames = "email")
)
public class Utilisateur implements UserDetails {

    @Transient
    public final static String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=_]).+$";

    @Transient
    public final static String EMAIL_PATTERN = "^[^@]+@[^@]+\\.[^@]+$";

    public Utilisateur(String testNom, String testPrenom, String password) {
        this.prenom = testPrenom;
        nom = testNom;
        motDePasse = password;
    }

    /**
     * Identifiant unique de l'utilisateur.
     * Généré automatiquement par la base de données.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Nom de l'utilisateur.
     * Ne peut pas être nul.
     */
    @NotBlank(message = "Le nom ne peut pas être vide")
    @Column(nullable = false)
    private String nom;

    /**
     * Prénom de l'utilisateur.
     * Ne peut pas être nul.
     */
    @NotBlank(message = "Le prénom ne peut pas être vide")
    @Column(nullable = false)
    private String prenom;

    /**
     * Adresse email de l'utilisateur
     * Ne peut pas être nul
     */
    @Email(message = "L'email doit être valide", regexp = EMAIL_PATTERN)
    @NotBlank(message = "L'email ne peut pas être vide")
    @Column(nullable = false)
    private String email;

    /**
     * Mot de passe de l'utilisateur.
     */
    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    @Pattern(regexp = PASSWORD_PATTERN, message = "Le mot de passe est invalide")
    @Column(nullable = false)
    private String motDePasse;

    /**
     * Libellé de l'adresse de l'utilisateur.
     */
    @NotBlank(message = "Le libellé de l'adresse ne peut pas être vide")
    @Column(nullable = false)
    private String libelleAdresse;

    /**
     * Code postal de l'utilisateur.
     */
    @NotBlank(message = "Le code postal ne peut pas être vide")
    @Pattern(regexp = "^[0-9]{5}$", message = "Le code postal doit être un code postal français valide")
    @Column(nullable = false)
    private String codePostal;

    /**
     * Ville de l'utilisateur.
     */
    @NotBlank(message = "La ville ne peut pas être vide")
    @Column(nullable = false)
    private String ville;

    /**
     * Longitude des coordonnées de l'utilisateur.
     */
    private double longitude;

    /**
     * Latitude des coordonnées de l'utilisateur.
     */
    private double latitude;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return motDePasse;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
