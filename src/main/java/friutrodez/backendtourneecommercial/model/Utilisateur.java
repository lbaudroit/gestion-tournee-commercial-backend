package friutrodez.backendtourneecommercial.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;


/**
 * Classe représentant un utilisateur.
 * Cette classe est utilisée pour mapper la table `Utilisateur` dans la base de données.
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
public class Utilisateur {

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
    @Column(nullable = false)
    private String nom;

    /**
     * Prénom de l'utilisateur.
     * Ne peut pas être nul.
     */
    @Column(nullable = false)
    private String prenom;

    /**
     * Mot de passe de l'utilisateur.
     */
    private String motDePasse;

    /**
     * Numéro de téléphone de l'utilisateur.
     */
    private String telephone;

    /**
     * Libellé de l'adresse de l'utilisateur.
     */
    private String libelleAdresse;

    /**
     * Code postal de l'utilisateur.
     */
    private String codePostal;

    /**
     * Ville de l'utilisateur.
     */
    private String ville;

    /**
     * Longitude des coordonnées de l'utilisateur.
     * TODO créer un record(Coordonnees) avec les coordonnées
     */
    private Float longitude;

    /**
     * Latitude des coordonnées de l'utilisateur.
     */
    private Float latitude;
}