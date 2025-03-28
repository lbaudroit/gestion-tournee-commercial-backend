package friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Enumération des algorithmes disponibles pour la génération d'itinéraires.
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
public enum AvailableAlgorithm {
    BRUTE_FORCE,
    BRUTE_FORCE_BRANCH_AND_BOUND,
    BRUTE_FORCE_BRANCH_AND_BOUND_PARALLEL,
    LITTLE,
    LITTLEV2;

    @Override
    public String toString() {
        return switch (this) {
            case BRUTE_FORCE -> "Brute force";
            case BRUTE_FORCE_BRANCH_AND_BOUND -> "Brute force branch and bound";
            case BRUTE_FORCE_BRANCH_AND_BOUND_PARALLEL -> "Brute force branch and bound parallel";
            case LITTLE -> "Little";
            case LITTLEV2 -> "Little v2";
        };
    }

    /**
     * Retourne la méthode de génération d'itinéraire correspondant à l'algorithme sélectionné.
     *
     * @return La méthode de génération d'itinéraire.
     * @throws NoSuchMethodException si la méthode n'existe pas.
     */
    public Method getAlgorithm() throws NoSuchMethodException {
        Algorithm algorithm = switch (this) {
            case BRUTE_FORCE -> new BruteForce();
            case BRUTE_FORCE_BRANCH_AND_BOUND -> new BruteForceBranchAndBound();
            case BRUTE_FORCE_BRANCH_AND_BOUND_PARALLEL -> new BruteForceBranchAndBoundParallel();
            case LITTLE -> new Little();
            case LITTLEV2 -> new LittleV2();
        };
        return algorithm.getClass().getMethod("generate", List.class, Point.class);
    }
}
