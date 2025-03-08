package friutrodez.backendtourneecommercial.dto;

import friutrodez.backendtourneecommercial.model.EtapesParcours;
import friutrodez.backendtourneecommercial.model.Parcours;
import org.springframework.data.mongodb.core.geo.GeoJsonLineString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public record ParcoursReducedDTO(String id, String nom, String date) {
    // Generate dto from a parcours
    public static ParcoursReducedDTO fromParcour(Parcours parcour) {
        // Date equals the date (not hour) of the start
        String date = parcour.getDateDebut().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return new ParcoursReducedDTO(parcour.get_id(), parcour.getNom(), date);
    }

    public static List<ParcoursReducedDTO> fromParcours(List<Parcours> parcours) {
        List<ParcoursReducedDTO> dtos = new ArrayList<>();
        for (Parcours parcour : parcours) {
            dtos.add(fromParcour(parcour));
        }
        return dtos;
    }
}