package friutrodez.backendtourneecommercial.service.algorithmeVoyageur;

public enum AlgoVoyageur {
    BRUTE_FORCE,
    BRUTE_FORCE_BRANCH_AND_BOUND,
    BRUTE_FORCE_BRANCH_AND_BOUND_PARALLEL,
    LITTLE,
    LITTLE_PARALLEL;

    @Override
    public String toString() {
        return switch (this) {
            case BRUTE_FORCE -> "Brute force";
            case BRUTE_FORCE_BRANCH_AND_BOUND -> "Brute force branch and bound";
            case BRUTE_FORCE_BRANCH_AND_BOUND_PARALLEL -> "Brute force branch and bound parallel";
            case LITTLE -> "Little";
            case LITTLE_PARALLEL -> "Little parallel";
            default -> throw new IllegalArgumentException();
        };
    }
}
