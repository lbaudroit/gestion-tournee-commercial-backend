package friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects.BenchMarkResults;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class BenchMarkResultsTest {

    @Test
    void display() {
        List<String> headers = new ArrayList<>();
        headers.add("Algorithme");
        headers.add("3 Noeuds");
        headers.add("4 Noeuds");
        List<Integer> results1 = new ArrayList<>();
        results1.add(null);
        results1.add(243272742);
        BenchMarkResults benchMarkResults = new BenchMarkResults(headers);
        benchMarkResults.addLine("Bruteforceeheheh", results1);
        System.out.println(benchMarkResults.display());
    }

    @Test
    void csv() {
        List<String> headers = new ArrayList<>();
        headers.add("Algorithme");
        headers.add("3 Noeuds");
        headers.add("4 Noeuds");
        List<Integer> results1 = new ArrayList<>();
        results1.add(null);
        results1.add(243272742);
        BenchMarkResults benchMarkResults = new BenchMarkResults(headers);
        benchMarkResults.addLine("Bruteforceeheheh", results1);
        System.out.println(benchMarkResults.csv());
    }
}