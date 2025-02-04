package friutrodez.backendtourneecommercial.dto;

import friutrodez.backendtourneecommercial.model.Client;

import java.util.List;

public record ItineraireDTO(long id, String nom, List<Client> clients, int distance) {
}
