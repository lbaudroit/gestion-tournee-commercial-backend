package friutrodez.backendtourneecommercial.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Itineraire {

    @Id
    @GeneratedValue
    private long id;


    @Column(nullable = false)
    private String nom;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id",nullable = false)
    private Utilisateur utilisateur;

}
