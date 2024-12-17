package friutrodez.backendtourneecommercial.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    private Coordonnees coordonnees;

    private Contact contact;

    /**
     * Le mapping en json se fera automatiquement
     * Null n'est pas possible
     */
    private boolean clientEffectif;

    /**
     * Vérifie si l'objet en argument est égaux à l'instance actuelle
     * @param object à vérifier
     * @return True si l'objet est égaux
     */
    @Override
    public boolean equals(Object object) {
        if(object instanceof Client client) {
            // Vérifie en priorité les ids
            if(client.get_id() != null && this.get_id() != null ) {
                return Objects.equals(get_id(),client.get_id());
            }
            // Vérifie en priorité les coordonnées
            if(coordonnees != null && client.coordonnees != null) {
                return  coordonnees.equals(client.coordonnees);
            }
            // TODO equals de Adresse et contact
            return Objects.equals(nomEntreprise,client.nomEntreprise);

        }
        return false;
    }
}
