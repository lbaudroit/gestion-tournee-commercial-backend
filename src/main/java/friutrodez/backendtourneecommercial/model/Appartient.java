package friutrodez.backendtourneecommercial.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Appartient {

    @EmbeddedId
    private AppartientKey idEmbedded;

    private int position;
}

