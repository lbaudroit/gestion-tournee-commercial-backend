package friutrodez.backendtourneecommercial.model;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe représentant un contact.
 * Cette classe contient les informations de base d'un contact telles que le nom, le prénom et le numéro de téléphone.
 * Utilise Lombok pour générer les constructeurs, getters et setters.
 *
 * @author Benjamin NICOL
 * Enzo CLUZEL
 * Leïla BAUDROIT
 * Ahmed BRIBACH
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Contact {

    @Transient
    public static final String PHONE_NUMBER_PATTERN = "[0-9]{10}";
    /**
     * Le nom du contact.
     */
    @NotNull(message = "Le nom ne peut pas être vide")
    @NotBlank(message = "Le nom ne peut pas être vide")
    private String nom;

    /**
     * Le prénom du contact.
     */
    @NotNull(message = "Le prenom e ne peut pas être vide")
    @NotBlank(message = "Le prenom ne peut pas être vide")
    private String prenom;

    /**
     * Le numéro de téléphone du contact.
     */
    @Pattern(regexp = PHONE_NUMBER_PATTERN, message = "Le numéro de téléphone n'est pas valide")
    @NotNull(message = "Le numéro de téléphone ne peut pas être vide")
    @NotBlank(message = "Le numéro de téléphone ne peut pas être vide")
    private String numeroTelephone;
}
