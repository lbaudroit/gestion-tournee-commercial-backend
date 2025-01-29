package friutrodez.backendtourneecommercial.dto;

import friutrodez.backendtourneecommercial.model.Client;

import java.util.List;

public record ResultatOptimisation(List<Client> clients, int kilometres) {
}
