package friutrodez.backendtourneecommercial.service.itineraryGenerator.objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Classe de test pour la classe Point.
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
class PointTest {

    /**
     * Test de la méthode addPoint pour ajouter des points et vérifier les distances.
     */
    @Test
    void addPoint() {
        Point point1 = new Point("tmp", 2.5732901, 44.3602355);
        Point point2 = new Point("tmp2", 3.0666963, 44.3223815);
        Point point3 = new Point("tmp3", 2.5731058, 44.3489985);
        point1.addPoint(point2);
        point1.addPoint(point3);

        Assertions.assertEquals(39463, point1.getDistance(point2));
        Assertions.assertEquals(1250, point1.getDistance(point3));
    }

    /**
     * Test de la méthode getDistance pour vérifier la distance entre deux points.
     */
    @Test
    void getDistance() {
        Point point1 = new Point("tmp", 2.5732901, 44.3602355);
        Point point2 = new Point("tmp2", 3.0666963, 44.3223815);
        point1.addPoint(point2);

        Assertions.assertEquals(39463, point1.getDistance(point2));
    }

    /**
     * Test de la méthode addPoint pour ajouter le même point et vérifier la distance.
     */
    @Test
    void addPoint_samePoint() {
        Point point1 = new Point("tmp", 2.5732901, 44.3602355);
        point1.addPoint(point1);

        Assertions.assertEquals(0, point1.getDistance(point1));
    }

    /**
     * Test de la méthode addPoint pour ajouter un point null et vérifier l'exception.
     */
    @Test
    void addPoint_nullPoint() {
        Point point1 = new Point("tmp", 2.5732901, 44.3602355);

        Assertions.assertThrows(NullPointerException.class, () -> point1.addPoint(null));
    }

    /**
     * Test de la méthode equals pour vérifier l'égalité avec le même objet.
     */
    @Test
    void equals_sameObject() {
        Point point1 = new Point("tmp", 2.5732901, 44.3602355);

        Assertions.assertEquals(point1, point1);
    }

    /**
     * Test de la méthode equals pour vérifier l'égalité avec un objet différent mais ayant le même identifiant.
     */
    @Test
    void equals_differentObjectSameId() {
        Point point1 = new Point("tmp", 2.5732901, 44.3602355);
        Point point2 = new Point("tmp", 3.0666963, 44.3223815);

        Assertions.assertEquals(point1, point2);
    }

    /**
     * Test de la méthode equals pour vérifier l'inégalité avec un objet ayant un identifiant différent.
     */
    @Test
    void equals_differentObjectDifferentId() {
        Point point1 = new Point("tmp", 2.5732901, 44.3602355);
        Point point2 = new Point("tmp2", 3.0666963, 44.3223815);

        Assertions.assertNotEquals(point1, point2);
    }
}