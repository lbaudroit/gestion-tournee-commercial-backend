package friutrodez.backendtourneecommercial.service.itineraryGenerator.algorithms;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour AvaibleAlgorithm.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
class AvaibleAlgorithmTest {

    /**
     * Teste que la méthode toString() retourne la chaîne correcte.
     */
    @Test
    void toStringReturnsCorrectString() {
        assertEquals("Brute force", AvailableAlgorithm.BRUTE_FORCE.toString());
        assertEquals("Brute force branch and bound", AvailableAlgorithm.BRUTE_FORCE_BRANCH_AND_BOUND.toString());
        assertEquals("Brute force branch and bound parallel", AvailableAlgorithm.BRUTE_FORCE_BRANCH_AND_BOUND_PARALLEL.toString());
        assertEquals("Little", AvailableAlgorithm.LITTLE.toString());
    }

    /**
     * Teste que la méthode getAlgorithm() retourne la méthode correcte.
     *
     * @throws NoSuchMethodException si la méthode n'existe pas
     */
    @Test
    void getAlgorithmReturnsCorrectMethod() throws NoSuchMethodException {
        Method bruteForceMethod = AvailableAlgorithm.BRUTE_FORCE.getAlgorithm();
        assertEquals("generate", bruteForceMethod.getName());
        assertArrayEquals(new Class[]{List.class, Point.class}, bruteForceMethod.getParameterTypes());

        Method branchAndBoundMethod = AvailableAlgorithm.BRUTE_FORCE_BRANCH_AND_BOUND.getAlgorithm();
        assertEquals("generate", branchAndBoundMethod.getName());
        assertArrayEquals(new Class[]{List.class, Point.class}, branchAndBoundMethod.getParameterTypes());

        Method parallelMethod = AvailableAlgorithm.BRUTE_FORCE_BRANCH_AND_BOUND_PARALLEL.getAlgorithm();
        assertEquals("generate", parallelMethod.getName());
        assertArrayEquals(new Class[]{List.class, Point.class}, parallelMethod.getParameterTypes());

        Method littleMethod = AvailableAlgorithm.LITTLE.getAlgorithm();
        assertEquals("generate", littleMethod.getName());
        assertArrayEquals(new Class[]{List.class, Point.class}, littleMethod.getParameterTypes());
    }

    /**
     * Teste que getAlgorithm() lance une RuntimeException pour une méthode invalide.
     */
    @Test
    void getAlgorithmThrowsRuntimeExceptionForInvalidMethod() {
        AvailableAlgorithm invalidAlgorithm = AvailableAlgorithm.BRUTE_FORCE;
        assertThrows(NoSuchMethodException.class, () -> {
            Method method = invalidAlgorithm.getClass().getMethod("nonExistentMethod");
            method.invoke(invalidAlgorithm);
        });
    }
}