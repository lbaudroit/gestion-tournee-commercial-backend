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
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
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
     * Débute à 0
     */
    private int position;
}