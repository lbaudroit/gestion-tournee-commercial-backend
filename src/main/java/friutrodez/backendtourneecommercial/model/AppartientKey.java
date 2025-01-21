package friutrodez.backendtourneecommercial.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Clé composite pour l'entité Appartient.
 * Cette classe est utilisée pour représenter la clé composite de l'entité Appartient.
 * Utilise Lombok pour générer les constructeurs, getters et setters.
 *
 * @author
 * Benjamin NICOL
 * Enzo CLUZEL
 * Leïla BAUDROIT
 * Ahmed BRIBACH
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class AppartientKey implements Serializable {

    /**
     * L'itinéraire associé à cette clé composite.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    private Itineraire itineraire;

    /**
     * L'identifiant du client associé à cette clé composite.
     */
    private String clientId;

}