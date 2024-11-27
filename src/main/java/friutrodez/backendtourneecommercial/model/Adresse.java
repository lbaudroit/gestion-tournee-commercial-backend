package friutrodez.backendtourneecommercial.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Adresse {
    private String libelle;

    private String codePostal;

    private String ville;


}
