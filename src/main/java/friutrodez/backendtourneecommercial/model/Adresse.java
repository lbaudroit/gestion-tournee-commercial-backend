package friutrodez.backendtourneecommercial.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe représentant une adresse.
 * Cette classe contient les informations de base d'une adresse telles que le libellé, le code postal et la ville.
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
public class Adresse {
    /**
     * Le libellé de l'adresse.
     */
    private String libelle;

    /**
     * Le code postal de l'adresse.
     */
    private String codePostal;

    /**
     * La ville de l'adresse.
     */
    private String ville;
}