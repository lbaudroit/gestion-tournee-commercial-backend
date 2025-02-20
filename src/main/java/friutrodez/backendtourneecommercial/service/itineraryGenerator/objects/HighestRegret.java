package friutrodez.backendtourneecommercial.service.itineraryGenerator.objects;

/**
 * Record representing the highest regret in the itinerary generation process.
 * Used in the Little algorithm to store the points and the regret value.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 *
 * @param ligne   The point representing the row.
 * @param colonne The point representing the column.
 * @param regret  The regret value associated with the points.
 */
public record HighestRegret(Point ligne, Point colonne, int regret) {
}