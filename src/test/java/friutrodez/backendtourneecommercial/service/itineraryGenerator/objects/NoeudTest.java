package friutrodez.backendtourneecommercial.service.itineraryGenerator.objects;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.ApiRequest;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects.TestData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NoeudTest {

    TestData testData = new TestData();
    Noeud noeud;

    @BeforeEach
    void setUp() {
        noeud = new Noeud(testData.getXRandPoints(4, testData.getStartEnd()));
    }

    @Test
    void getSizeMatrix() {
        assertEquals(4, noeud.getSizeMatrix());
    }

    @Test
    void getLowestValueNode() {

    }

    @Test
    void expand() {
        noeud.expand();
        Noeud left = noeud.left;
        Noeud right = noeud.right;
        System.out.println(noeud.getValue());
        System.out.println(left.getValue());
        System.out.println(right.getValue());
    }

    @Test
    void getValue() {
    }
}