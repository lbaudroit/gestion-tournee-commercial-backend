package friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Classe de test pour BenchMarkResults.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
class BenchMarkResultsTest {

    /**
     * Teste l'affichage avec des résultats vides.
     */
    @Test
    void displayWithEmptyResults() {
        List<String> headers = new ArrayList<>();
        headers.add("Algorithme");
        BenchMarkResults benchMarkResults = new BenchMarkResults(headers);
        assertEquals("No benchmark results available.", benchMarkResults.display());
    }

    /**
     * Teste l'affichage avec un seul résultat.
     */
    @Test
    void displayWithSingleResult() {
        List<String> headers = new ArrayList<>();
        headers.add("Algorithme");
        headers.add("3 Noeuds");
        headers.add("4 Noeuds");
        headers.add("5 Noeuds");
        List<Long> results1 = new ArrayList<>();
        results1.add(243272742L);
        results1.add(243272742L);
        results1.add(243272742L);
        BenchMarkResults benchMarkResults = new BenchMarkResults(headers);
        benchMarkResults.addLine("Bruteforce", results1);
        String display = benchMarkResults.display();
        assertTrue(display.contains("Bruteforce"));
        assertTrue(display.contains("0.243272742") || display.contains("0,243272742"));
    }

    /**
     * Teste la génération de CSV avec des résultats vides.
     */
    @Test
    void csvWithEmptyResults() {
        List<String> headers = new ArrayList<>();
        headers.add("Algorithme");
        BenchMarkResults benchMarkResults = new BenchMarkResults(headers);
        assertEquals("Algorithme\n", benchMarkResults.csv());
    }

    /**
     * Teste la génération de CSV avec un seul résultat.
     */
    @Test
    void csvWithSingleResult() {
        List<String> headers = new ArrayList<>();
        headers.add("Algorithme");
        headers.add("3 Noeuds");
        List<Long> results1 = new ArrayList<>();
        results1.add(243272742L);
        BenchMarkResults benchMarkResults = new BenchMarkResults(headers);
        benchMarkResults.addLine("Bruteforce", results1);
        String csv = benchMarkResults.csv();
        assertTrue(csv.contains("Bruteforce;0.243272742") || csv.contains("Bruteforce;0,243272742"));
    }

    /**
     * Teste l'ajout d'une ligne avec moins de valeurs que d'en-têtes.
     */
    @Test
    void addLineWithFewerValuesThanHeaders() {
        List<String> headers = new ArrayList<>();
        headers.add("Algorithme");
        headers.add("3 Noeuds");
        headers.add("4 Noeuds");
        List<Long> results1 = new ArrayList<>();
        results1.add(243272742L);
        BenchMarkResults benchMarkResults = new BenchMarkResults(headers);
        benchMarkResults.addLine("Bruteforce", results1);
        String display = benchMarkResults.display();
        assertTrue(display.contains("Bruteforce"));
        assertTrue(display.contains("0.243272742") || display.contains("0,243272742"));
        assertTrue(display.contains("N/A"));
    }

    /**
     * Teste l'écriture des résultats dans un fichier.
     */
    @Test
    void testWriteResultsToFile() {
        List<String> headers = new ArrayList<>();
        headers.add("Algorithme");
        headers.add("3 Noeuds");
        headers.add("4 Noeuds");
        headers.add("5 Noeuds");
        List<Long> results1 = new ArrayList<>();
        results1.add(243272742L);
        results1.add(243272742L);
        results1.add(243272742L);
        BenchMarkResults benchMarkResults = new BenchMarkResults(headers);
        benchMarkResults.addLine("Bruteforce", results1);
        benchMarkResults.writeResultsToFile("test.csv");
        File file = new File("test.csv");
        assertTrue(file.exists());
        file.delete();
    }
}