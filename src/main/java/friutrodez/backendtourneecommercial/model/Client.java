package friutrodez.backendtourneecommercial.model;

import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="client")
public class Client {

    @Id
    private String _id;

    private String idUtilisateur;

    private String nomEntreprise;

    private Adresse adresse;

    private String descriptif;

    private double[] coordonnees;

    private Contact contact;

    private boolean clientEffectif;

    /**
     * Vérifie si l'objet en argument est égaux à l'instance actuelle
     * @param object
     * @return True si l'objet à le même nom
     */
    @Override
    public boolean equals(Object object) {
        if(object instanceof Client) {
            Client client = (Client) object;
            // Vérifie en priorité les coordonnées
            if(coordonnees == null) {
                return Objects.equals(nomEntreprise,client.nomEntreprise);
            }
            // TODO equals de Adresse et contact
            return Arrays.equals(coordonnees, client.coordonnees);

        }
        return false;
    }
}
