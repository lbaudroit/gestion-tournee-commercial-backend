package friutrodez.backendtourneecommercial.dto;

import friutrodez.backendtourneecommercial.model.EtapesParcours;
import org.springframework.data.mongodb.core.geo.GeoJsonLineString;

import java.time.LocalDateTime;
import java.util.List;

public record ParcoursDTO(List<EtapesParcours> etapes, String nom, GeoJsonLineString chemin, LocalDateTime debut, LocalDateTime fin) {}