package friutrodez.backendtourneecommercial.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Classe représentant un itinéraire.
 * Un itinéraire est un chemin entre plusieurs clients et le commercial.
 * Cette classe est utilisée pour mapper la table `Itineraire` dans la base de données.
 * Utilise Lombok pour générer les constructeurs, getters, setters et le builder.
 * <p>
 * L'itineraire doit avoir
 * – Un nom non vide
 * – Une distance non vide et non négative
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Itineraire {

    /**
     * Le nombre maximal de clients pouvant être contenus dans un itinéraire
     */
    @Transient
    public static final int MAX_CLIENTS = 8;

    /**
     * Identifiant unique de l'itinéraire.
     * Généré automatiquement par la base de données.
     */
    @Id
    @GeneratedValue
    private long id;

    /**
     * Nom de l'itinéraire.
     * Ne peut pas être nul.
     */
    @Column(nullable = false)
    @NotBlank(message = "Le nom ne peut pas être vide")
    @NotNull(message = "Le nom ne peut pas être vide")
    private String nom;

    @Column(nullable = false)
    @NotNull(message = "La distance ne peut pas être vide")
    private Integer distance;

    /**
     * Utilisateur associé à l'itinéraire.
     * Utilise une relation OneToOne avec une fusion en cascade.
     * La colonne `id_utilisateur` ne peut pas être nulle.
     */
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private Utilisateur utilisateur;

}