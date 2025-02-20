package friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Classe pour stocker et gérer les résultats des benchmarks.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
public class BenchMarkResults {
    private static final double NANO_TO_SEC = 1_000_000_000;
    public static final int MIN_COLUMN_WIDTH = 11;
    public static final int MARGIN = 2;
    public static final String DELIMITER = ";";
    private final List<String> headers;
    private final HashMap<String, List<Long>> lines;

    /**
     * Constructeur pour initialiser les résultats des benchmarks avec des en-têtes.
     *
     * @param headers Liste des en-têtes pour les résultats des benchmarks.
     */
    public BenchMarkResults(List<String> headers) {
        this.headers = headers;
        this.lines = new HashMap<>();
    }

    /**
     * Ajoute une ligne de résultats de benchmark.
     * Si la liste de valeurs est plus courte que le nombre d'en-têtes, des valeurs nulles sont ajoutées.
     *
     * @param key    La clé représentant le benchmark.
     * @param values La liste des valeurs pour le benchmark.
     */
    public void addLine(String key, List<Long> values) {
        while (values.size() < headers.size() - 1) {
            values.add(null);
        }
        lines.put(key, new ArrayList<>(values));
    }

    /**
     * Affiche les résultats des benchmarks dans un tableau formaté.
     *
     * @return Une chaîne représentant les résultats des benchmarks formatés.
     */
    public String display() {
        if (lines.isEmpty()) return "No benchmark results available.";

        StringBuilder sb = new StringBuilder("Benchmark results:\n");
        int maxHeaderLength = headers.stream().mapToInt(String::length).max().orElse(0);
        for (String key : lines.keySet()) {
            maxHeaderLength = Math.max(maxHeaderLength, key.length());
        }
        maxHeaderLength = Math.max(maxHeaderLength, MIN_COLUMN_WIDTH);
        int columnWidth = maxHeaderLength + MARGIN;
        int tableWidth = columnWidth * headers.size() + headers.size() + 1;

        sb.append("┌").append("─".repeat(tableWidth - 2)).append("┐\n│");
        for (String header : headers) {
            int padding = (maxHeaderLength - header.length()) / 2;
            sb.append(String.format(" %" + (padding != 0 ? padding : "") + "s%-" + (maxHeaderLength - padding) + "s │", "", header));
        }
        sb.append("\n├").append("─".repeat(tableWidth - 2)).append("┤\n");

        List<String> sortedKeys = new ArrayList<>(lines.keySet());
        Collections.sort(sortedKeys);
        for (String key : sortedKeys) {
            sb.append("│").append(String.format(" %-" + maxHeaderLength + "s │", key));
            for (Long value : lines.get(key)) {
                String valueStr = value != null ? String.format("%.9f", value / NANO_TO_SEC) : "N/A";
                int padding = (maxHeaderLength - valueStr.length()) / 2;
                sb.append(String.format(" %" + (padding != 0 ? padding : "") + "s%-" + (maxHeaderLength - padding) + "s │", "", valueStr));
            }
            sb.append("\n");
        }
        sb.append("└").append("─".repeat(tableWidth - 2)).append("┘\n");

        return sb.toString();
    }

    /**
     * Génère une représentation CSV des résultats des benchmarks.
     *
     * @return Une chaîne représentant les résultats des benchmarks formatés en CSV.
     */
    public String csv() {
        StringBuilder sb = new StringBuilder(String.join(DELIMITER, headers)).append("\n");
        for (String key : lines.keySet()) {
            sb.append(key).append(DELIMITER);
            for (Long value : lines.get(key)) {
                sb.append(value != null ? String.format("%.9f", value / NANO_TO_SEC) : "").append(DELIMITER);
            }
            sb.deleteCharAt(sb.length() - 1).append("\n");
        }
        return sb.toString();
    }

    /**
     * Écrit les résultats des benchmarks dans un fichier.
     *
     * @param fileName Le nom du fichier dans lequel écrire les résultats.
     */
    public void writeResultsToFile(String fileName) {
        try (PrintWriter writer = new PrintWriter(fileName)) {
            writer.write(csv());
        } catch (Exception e) {
            System.err.println("Failed to write benchmark results to file: " + e.getMessage());
        }
    }
}