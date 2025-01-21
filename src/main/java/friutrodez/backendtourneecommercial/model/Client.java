package friutrodez.backendtourneecommercial.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Transient;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Classe représentant un client.
 * Cette classe est utilisée pour mapper la collection `client` dans la base de données MongoDB.
 * Utilise Lombok pour générer les constructeurs, getters, setters et le builder.
 *
 * @author
 * Benjamin NICOL
 * Enzo CLUZEL
 * Leïla BAUDROIT
 * Ahmed BRIBACH
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection="client")
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
    private String nomEntreprise;

    /**
     * Adresse du client.
     */
    private Adresse adresse;

    /**
     * Descriptif du client.
     */
    private String descriptif;

    /**
     * Coordonnées géographiques du client.
     */
    private double[] coordonnees;

    /**
     * Contact du client.
     */
    private Contact contact;

    /**
     * Indique si le client est effectif.
     */
    private boolean clientEffectif;

}