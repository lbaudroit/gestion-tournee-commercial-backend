package friutrodez.backendtourneecommercial.dto;

/**
 * DTO pour la création d'un itinéraire
 *
 * @param nom       nom de l'itinéraire
 * @param idClients liste des identifiants des clients
 * @param distance  distance totale de l'itinéraire
 */
public record ItineraireCreationDTO(String nom, String[] idClients, int distance) {
}
