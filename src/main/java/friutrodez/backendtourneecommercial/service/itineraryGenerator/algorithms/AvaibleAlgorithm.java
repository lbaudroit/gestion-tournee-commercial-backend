package friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;

import java.lang.reflect.Method;
import java.util.List;

public enum AvaibleAlgorithm {
    BRUTE_FORCE,
    BRUTE_FORCE_BRANCH_AND_BOUND,
    BRUTE_FORCE_BRANCH_AND_BOUND_PARALLEL,
    LITTLE;

    @Override
    public String toString() {
        return switch (this) {
            case BRUTE_FORCE -> "Brute force";
            case BRUTE_FORCE_BRANCH_AND_BOUND -> "Brute force branch and bound";
            case BRUTE_FORCE_BRANCH_AND_BOUND_PARALLEL -> "Brute force branch and bound parallel";
            case LITTLE -> "Little";
            default -> throw new IllegalArgumentException();
        };
    }

    public Method getAlgorithm() {
        Algorithm tmp =  switch (this) {
            case BRUTE_FORCE -> new friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms.BruteForce();
            case BRUTE_FORCE_BRANCH_AND_BOUND -> new friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms.BruteForceBranchAndBound();
            case BRUTE_FORCE_BRANCH_AND_BOUND_PARALLEL -> new friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms.BruteForceBranchAndBoundParallel();
            case LITTLE -> new friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms.Little();
            default -> throw new IllegalArgumentException();
        };
        try {
            return tmp.getClass().getMethod("generate", List.class, Point.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
