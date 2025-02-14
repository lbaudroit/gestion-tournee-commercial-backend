package friutrodez.backendtourneecommercial.service.itineraryGenerator.utils;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects.BenchMarkResults;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class BenchmarkTest {
    private static Point startEnd;

    @BeforeAll
    static void setUp() {
        startEnd = new Point("startEnd", 2.8847171, 43.9596889);
    }

    @Test
    void benchmark() {
        BenchMarkResults benchMarkResults = Benchmark.benchmark(startEnd);
        System.out.println(benchMarkResults.display());
        System.out.println(benchMarkResults.csv());
    }
}