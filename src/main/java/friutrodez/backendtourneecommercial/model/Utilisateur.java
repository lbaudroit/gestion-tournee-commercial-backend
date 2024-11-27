package friutrodez.backendtourneecommercial.model;

import com.mongodb.lang.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Utilisateur {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String motDePasse;

    @Column(nullable = false)
    private String telephone;

    @Column(nullable = false)
    private String libelleAdresse;

    @Column(nullable = false)
    private String codePostal;

    @Column(nullable = false)
    private String ville;


}
