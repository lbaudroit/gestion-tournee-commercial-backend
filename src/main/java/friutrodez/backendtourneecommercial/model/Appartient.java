package friutrodez.backendtourneecommercial.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe représentant l'entité Appartient.
 * Cette classe est utilisée pour mapper la table `Appartient` dans la base de données.
 * Utilise Lombok pour générer les constructeurs, getters et setters.
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Appartient {

    /**
     * Clé composite pour l'entité Appartient.
     */
    @EmbeddedId
    private AppartientKey idEmbedded;

    /**
     * Position de l'élément dans l'itinéraire.
     * Débute à zéro
     */
    private int position;
}