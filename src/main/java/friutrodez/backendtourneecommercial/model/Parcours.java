package friutrodez.backendtourneecommercial.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Document(collection = "Parcours")
public class Parcours {

    @Id
    private String id;

    private long idUtilisateur;

    private String nom ;

    private EtapesParcours[] etapes;

    private double[][] coordonnees;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateDebut;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateFin;
}

