package friutrodez.backendtourneecommercial.model;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Appartient {

    @ManyToOne
    @JoinColumn(name = "id")
    private Itineraire itineraire;

    private String idClient;
}
