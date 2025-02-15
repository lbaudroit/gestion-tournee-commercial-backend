package friutrodez.backendtourneecommercial.service.itineraryGenerator.objects;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    TestData testData;
    List<Point> points1;
    Node racine1;
    List<Point> points2;
    Node racine2;
    List<Point> points3;
    Node racine3;
    Point startEnd;

    @BeforeEach
    void setUp() {
        testData = new TestData();
        startEnd = testData.getStartEnd();
        points1 = testData.getStaticPoints1(startEnd);
        racine1 = new Node(points1);
        points2 = testData.getStaticPoints2(startEnd);
        racine2 = new Node(points2);
        points3 = testData.getStaticPoints3(startEnd);
        racine3 = new Node(points3);
    }


    @Test
    void testGetSizeMatrix() {
        assertEquals(5, racine1.getSizeMatrix());
        assertEquals(3, racine2.getSizeMatrix());
        assertEquals(4, racine3.getSizeMatrix());
    }

    @Test
    void getLowestValueNode() {
        assertEquals(racine1, racine1.getLowestValueNode());
        assertEquals(108668, racine1.getLowestValueNode().getValue());
        assertEquals(racine2, racine2.getLowestValueNode());
        assertEquals(121800, racine2.getLowestValueNode().getValue());
        assertEquals(racine3, racine3.getLowestValueNode());
        assertEquals(91866, racine3.getLowestValueNode().getValue());
        racine1.expand();
        racine2.expand();
        racine3.expand();
        assertEquals(racine1.getLeft(), racine1.getLowestValueNode());
        assertEquals(126118, racine1.getLowestValueNode().getValue());
        assertEquals(racine2.getLeft(), racine2.getLowestValueNode());
        assertEquals(140222, racine2.getLowestValueNode().getValue());
        assertEquals(racine3.getLeft(), racine3.getLowestValueNode());
        assertEquals(193523, racine3.getLowestValueNode().getValue());
    }

    @Test
    void expand() {
        racine1.expand();
        verifyRoot1Expand();
    }

    void verifyRoot1Expand() {
        // Vérification pour la gauche
        assertNotNull(racine1.getLeft());
        // Vérification du contenu de la matrice
        assertEquals(4, racine1.getLeft().getSizeMatrix());
        String matrixLeftExpected = "[[2147481944, 0, 0, 804], [0, 2147482206, 1248, 0], [804, 0, 2474, 2147482206], " +
                "[922, 0, 2147466328, 834]]";
        assertEquals(matrixLeftExpected, Arrays.deepToString(racine1.getLeft().getMatrixContent()));
        // Vérifications des point to index de la ligne
        assertTrue(racine1.getLeft().getPointToIndexRow().containsKey(points1.getFirst()));
        assertTrue(racine1.getLeft().getPointToIndexRow().containsKey(points1.get(1)));
        assertFalse(racine1.getLeft().getPointToIndexRow().containsKey(points1.get(2)));
        assertTrue(racine1.getLeft().getPointToIndexRow().containsKey(points1.get(3)));
        assertTrue(racine1.getLeft().getPointToIndexRow().containsKey(points1.getLast()));
        // Vérifications des point to index de la colonne
        assertTrue(racine1.getLeft().getPointToIndexColumn().containsKey(points1.getFirst()));
        assertTrue(racine1.getLeft().getPointToIndexColumn().containsKey(points1.get(1)));
        assertTrue(racine1.getLeft().getPointToIndexColumn().containsKey(points1.get(2)));
        assertTrue(racine1.getLeft().getPointToIndexColumn().containsKey(points1.get(3)));
        assertFalse(racine1.getLeft().getPointToIndexColumn().containsKey(points1.getLast()));
        // Vérification de la valeur du nœud
        assertEquals(126118, racine1.getLeft().getValue());
        // Vérification des points start et end
        assertEquals(points1.get(2), racine1.getLeft().getStart());
        assertEquals(points1.getLast(), racine1.getLeft().getEnd());
        // Vérification des nœuds fils
        assertNull(racine1.getLeft().getLeft());
        assertNull(racine1.getLeft().getRight());
        // Vérification du nœud parent
        assertEquals(racine1, racine1.getLeft().getParent());

        // Vérification pour la droite
        assertNotNull(racine1.getRight());
        // Vérification du contenu de la matrice
        assertEquals(5, racine1.getRight().getSizeMatrix());
        String matrixRightExpected = "[[2147482075, 0, 17319, 804, 922], [131, 2147482206, 18567, 0, 0]," +
                " [0, 1117, 2147452904, 2343, 2147466197], [935, 0, 19793, 2147482206, 834], " +
                "[1053, 0, 0, 834, 2147392726]]";
        assertEquals(matrixRightExpected, Arrays.deepToString(racine1.getRight().getMatrixContent()));
        // Vérifications que les même points sont présents
        assertEquals(racine1.getPointToIndexRow(), racine1.getRight().getPointToIndexRow());
        assertEquals(racine1.getPointToIndexColumn(), racine1.getRight().getPointToIndexColumn());
        // Vérification de la valeur du nœud
        assertEquals(126118, racine1.getRight().getValue());
        // Vérification des points start et end
        assertNull(racine1.getRight().getStart());
        assertNull(racine1.getRight().getEnd());
        // Vérification des nœuds fils
        assertNull(racine1.getRight().getLeft());
        assertNull(racine1.getRight().getRight());
        // Vérification du nœud parent
        assertEquals(racine1, racine1.getRight().getParent());

    }

    @Test
    void getAllNodesOnRoute() {
        racine1.expand();
        Node lowestValueNode1 = racine1.getLowestValueNode();
        assertEquals(1, lowestValueNode1.getAllNodesOnRoute().size());
        assertTrue(lowestValueNode1.getAllNodesOnRoute().contains(lowestValueNode1));
        lowestValueNode1.expand();
        Node lowestValueNode2 = racine1.getLowestValueNode();
        assertEquals(2, lowestValueNode2.getAllNodesOnRoute().size());
        assertTrue(lowestValueNode2.getAllNodesOnRoute().contains(lowestValueNode1));
        assertTrue(lowestValueNode2.getAllNodesOnRoute().contains(lowestValueNode2));
    }
}