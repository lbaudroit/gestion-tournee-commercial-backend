package friutrodez.backendtourneecommercial.dto;

import friutrodez.backendtourneecommercial.model.Client;

import java.util.List;

/**
 * DTO pour un itinéraire
 * @param id id de l'itinéraire
 * @param nom nom de l'itinéraire
 * @param clients liste des clients
 * @param distance distance totale de l'itinéraire
 */
public record ItineraireDTO(long id, String nom, List<Client> clients, int distance) {
}
