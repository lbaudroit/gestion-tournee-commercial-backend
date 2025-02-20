package friutrodez.backendtourneecommercial.model;

import jakarta.persistence.Transient;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonLineString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe représentant un parcours.
 * Cette classe est utilisée pour mapper la collection `Parcours` dans la base de données MongoDB.
 * Utilise Lombok pour générer les constructeurs, getters, setters et le builder.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "parcours")
public class Parcours {

    /**
     * Nom de la séquence utilisée pour générer les identifiants des parcours.
     * Cette séquence est utilisée par MongoDB pour l'auto-incrémentation des identifiants.
     */
    @Transient
    public static final String SEQUENCE_NAME = "parcours_sequence";

    /**
     * Identifiant unique du parcours.
     * Généré automatiquement par MongoDB.
     */
    @Id
    private String _id;

    /**
     * Identifiant de l'utilisateur associé au parcours.
     */
    private String idUtilisateur;

    /**
     * Nom du parcours.
     */
    private String nom;

    /**
     * Liste des étapes du parcours.
     */
    private List<EtapesParcours> etapes;

    /**
     * Coordonnées géographiques du parcours.
     */
    private GeoJsonLineString coordonnees;

    /**
     * Date et heure de début du parcours.
     * Format : yyyy-MM-dd HH:mm
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateDebut;

    /**
     * Date et heure de fin du parcours.
     * Format : yyyy-MM-dd HH:mm
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateFin;


}