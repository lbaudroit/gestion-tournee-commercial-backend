package friutrodez.backendtourneecommercial.service.itineraryGenerator.objects;

/**
 * Record representing the result of a matrix reduction operation.
 * Used in the Little algorithm to store the reduced matrix and the associated value.
 *
 * @param matrix The reduced matrix after the reduction operation.
 * @param value  The value associated with the reduction operation.
 */
public record ReduceReturn(int[][] matrix, int value) {
}