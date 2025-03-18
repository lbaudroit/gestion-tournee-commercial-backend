package friutrodez.backendtourneecommercial.dto;

import friutrodez.backendtourneecommercial.model.EtapesParcours;
import org.springframework.data.mongodb.core.geo.GeoJsonLineString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Parcours envoyé au client de l'API
 *
 * @param etapes liste des étapes du parcours
 * @param nom    nom du parcours
 * @param chemin chemin du parcours
 * @param debut  date de début du parcours
 * @param fin    date de fin du parcours
 */
public record ParcoursDTO(List<EtapesParcours> etapes, String nom, GeoJsonLineString chemin, LocalDateTime debut,
                          LocalDateTime fin) {
}