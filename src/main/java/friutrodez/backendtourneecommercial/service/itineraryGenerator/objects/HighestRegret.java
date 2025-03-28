package friutrodez.backendtourneecommercial.service.itineraryGenerator.objects;

/**
 * Record representing the highest regret in the itinerary generation process.
 * Used in the Little algorithm to store the points and the regret value.
 *
 * @param ligne   The point representing the row.
 * @param colonne The point representing the column.
 * @param regret  The regret value associated with the points.
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
public record HighestRegret(Point ligne, Point colonne, int regret) {
}