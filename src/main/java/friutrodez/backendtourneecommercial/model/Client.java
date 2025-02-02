package friutrodez.backendtourneecommercial.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

/**
 * Classe représentant un client.
 * Cette classe est utilisée pour mapper la collection `client` dans la base de données MongoDB.
 * Utilise Lombok pour générer les constructeurs, getters, setters et le builder.
 * <p>
 * Le client doit avoir :
 * - Une adresse valide en france
 * - Un nom d'entreprise non vide
 *
 * @author Benjamin NICOL
 * Enzo CLUZEL
 * Leïla BAUDROIT
 * Ahmed BRIBACH
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "client")
public class Client {

    /**
     * Nom de la séquence utilisée pour générer les identifiants des clients.
     * Cette séquence est utilisée par MongoDB pour l'auto-incrémentation des identifiants.
     */
    @Transient
    public static final String SEQUENCE_NAME = "client_sequence";

    /**
     * Identifiant unique du client.
     * Généré automatiquement par MongoDB.
     */
    @Id
    @GeneratedValue
    private String _id;

    /**
     * Identifiant de l'utilisateur associé au client.
     */
    private String idUtilisateur;

    /**
     * Nom de l'entreprise du client.
     */
    @NotBlank
    @Column(nullable = false)
    @NotNull
    private String nomEntreprise;

    /**
     * Adresse du client.
     */
    @Column(nullable = false)
    @NotNull(message = "L'adresse doit être rempli")
    @Valid
    private Adresse adresse;

    /**
     * Descriptif du client.
     */
    private String descriptif;

    /**
     * Coordonnées géographiques du client.
     */
    private Coordonnees coordonnees;

    /**
     * Contact du client.
     */
    @NotNull(message = "Le contact doit être rempli")
    @Valid
    private Contact contact;

    /**
     * Le mapping en json se fera automatiquement
     * Null n'est pas possible
     */
    private boolean clientEffectif;

    /**
     * Vérifie si l'objet en argument est égaux à l'instance actuelle
     *
     * @param object à vérifier
     * @return True si l'objet est égaux
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Client client) {
            // Vérifie en priorité les ids
            if (client.get_id() != null && this.get_id() != null) {
                return Objects.equals(get_id(), client.get_id());
            }
            // Vérifie en priorité les coordonnées
            if (coordonnees != null && client.coordonnees != null) {
                return coordonnees.equals(client.coordonnees);
            }
            // TODO equals de Adresse et contact
            return Objects.equals(nomEntreprise, client.nomEntreprise);

        }
        return false;
    }
}
