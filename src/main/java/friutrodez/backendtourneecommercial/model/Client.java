package friutrodez.backendtourneecommercial.model;

import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="client")
public class Client {

    @Id
    private String _id;

    private long idUtilisateur;

    private String nomEntreprise;

    private Adresse adresse;

    private String descriptif;

    private double[] coordonnees;

    private Contact contact;

    private boolean clientEffectif;



}
