package friutrodez.backendtourneecommercial.dto;

import java.util.List;

/**
 * Résultat de l'optimisation envoyé au client de l'API
 *
 * @param clients    Liste des clients dans l'ordre de passage
 * @param kilometres Nombre de kilomètres parcourus
 */
public record ResultatOptimisation(List<ClientId> clients, int kilometres) {
}
