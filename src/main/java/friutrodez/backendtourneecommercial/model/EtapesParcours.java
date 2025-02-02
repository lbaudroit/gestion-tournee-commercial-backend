package friutrodez.backendtourneecommercial.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe représentant une étape de parcours.
 * Cette classe contient les informations de base d'une étape de parcours telles que le nom, l'état de visite et les coordonnées.
 * Utilise Lombok pour générer les constructeurs, getters et setters.
 *
 * @author Benjamin NICOL
 * Enzo CLUZEL
 * Leïla BAUDROIT
 * Ahmed BRIBACH
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EtapesParcours {

    /**
     * Le nom de l'étape de parcours.
     */
    private String nom;

    /**
     * Indique si l'étape a été visitée.
     */
    private boolean visite;

    /**
     * Coordonnées géographiques de l'étape de parcours.
     */
    private double[] coordonnees;
}