package friutrodez.backendtourneecommercial.dto;

import friutrodez.backendtourneecommercial.model.EtapesParcours;

import java.util.List;

/**
 * Objet DTO (Data Transfert Object pour les parcours
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
public class ParcoursDTO {

    private List<EtapesParcours> etapes;

    private String nom;


    /**
     * Object parcours
     * @param nom
     * @param etapes
     */
    public ParcoursDTO(String nom , List<EtapesParcours> etapes) {
        this.nom = nom;
        this.etapes = etapes;
    }

    /**
     * Getter
     * @return etapes la listes des étapes d'un parcours
     */
    public List<EtapesParcours> getEtapes() {
        return etapes;
    }

    /**
     * Getter
     * @return nom le nom du parcours
     */
    public String getNom() {
        return nom;
    }

    /**
     * Setter
     * Affecte une liste d'etapes
     * @param etapes
     */
    public void setEtapes(List<EtapesParcours> etapes) {
        this.etapes = etapes;
    }

    /**
     * Setter
     * affecte le nom du parcours
     * @param name
     */
    public void setNom(String name) {
        this.nom = name;
    }
}