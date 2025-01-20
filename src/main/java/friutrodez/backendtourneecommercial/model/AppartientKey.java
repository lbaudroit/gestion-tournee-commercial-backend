package friutrodez.backendtourneecommercial.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class AppartientKey {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    private Itineraire itineraire;

    private String clientId;

}
