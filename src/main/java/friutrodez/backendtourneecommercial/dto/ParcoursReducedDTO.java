package friutrodez.backendtourneecommercial.dto;

import friutrodez.backendtourneecommercial.model.Parcours;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public record ParcoursReducedDTO(String id, String nom, String date) {
    /**
     * Convertit un parcours en DTO réduit.
     * @param parcours Parcours à convertir
     * @return DTO réduit du parcours
     */
    public static ParcoursReducedDTO fromParcours(Parcours parcours) {
        // Date equals the date (not hour) of the start
        String date = parcours.getDateDebut().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return new ParcoursReducedDTO(parcours.get_id(), parcours.getNom(), date);
    }

    /**
     * Convertit une liste de parcours en liste de DTO réduits.
     * @param parcours Liste de parcours à convertir
     * @return Liste de DTO réduits des parcours
     */
    public static List<ParcoursReducedDTO> fromParcours(List<Parcours> parcours) {
        List<ParcoursReducedDTO> dtos = new ArrayList<>();
        for (Parcours parcour : parcours) {
            dtos.add(fromParcours(parcour));
        }
        return dtos;
    }
}