package friutrodez.backendtourneecommercial.model;


import jakarta.persistence.*;
import lombok.*;

/**
 * Classe représentant un itinéraire.
 * Cette classe est utilisée pour mapper la table `Itineraire` dans la base de données.
 * Utilise Lombok pour générer les constructeurs, getters, setters et le builder.
 *
 * @author
 * Benjamin NICOL
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
public class Itineraire {

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
    private String nom;

    /**
     * Utilisateur associé à l'itinéraire.
     * Utilise une relation OneToOne avec une fusion en cascade.
     * La colonne `id_utilisateur` ne peut pas être nulle.
     */
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private Utilisateur utilisateur;

}