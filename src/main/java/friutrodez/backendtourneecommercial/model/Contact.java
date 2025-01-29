package friutrodez.backendtourneecommercial.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe représentant un contact.
 * Cette classe contient les informations de base d'un contact telles que le nom, le prénom et le numéro de téléphone.
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
public class Contact {

    /**
     * Le nom du contact.
     */
    private String nom;

    /**
     * Le prénom du contact.
     */
    private String prenom;
  
    /**
     * Le numéro de téléphone du contact.
     */
    private String numeroTelephone;
}
