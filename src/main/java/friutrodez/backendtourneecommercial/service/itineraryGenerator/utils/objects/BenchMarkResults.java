package friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.AlgoVoyageur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BenchMarkResults {
    List<String> headers;

    HashMap<String, List<Long>> results;

    public BenchMarkResults(List<String> headers) {
        this.headers = headers;
        this.results = new HashMap<>();
    }

    public void addLine(String key, List<Long> values) {
        if (values.size() == headers.size() - 1) {
            results.put(key, values);
        } else {
            for (int i = values.size(); i < headers.size() - 1; i++) {
                values.add(null);
            }
            results.put(key, values);
        }
    }

    public String display() {
        if (results.isEmpty()) {
            return "No benchmark results available.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Benchmark results:\n");

        int maxHeaderLength = headers.stream().mapToInt(String::length).max().orElse(0);
        for (String key : results.keySet()) {
            maxHeaderLength = Math.max(maxHeaderLength, key.length());
        }
        if (maxHeaderLength % 2 != 0) {
            maxHeaderLength++;
        }
        if (maxHeaderLength < 10) {
            maxHeaderLength = 10;
        }
        int columnWidth = maxHeaderLength + 2;
        int tableWidth = columnWidth * headers.size() + headers.size() + 1;
        sb.append("┌").append("─".repeat(tableWidth - 2)).append("┐\n");
        sb.append("│");
        for (String header : headers) {
            int padding = (maxHeaderLength - header.length()) / 2;
            sb.append(String.format(" %" + (padding != 0 ? padding : "") + "s%-" + (maxHeaderLength - padding) + "s │", "", header));
        }
        sb.append("\n");
        sb.append("├").append("─".repeat(tableWidth - 2)).append("┤\n");

        List<String> sortedKeys = new ArrayList<>(results.keySet());
        Collections.sort(sortedKeys);
        for (String key : sortedKeys) {
            sb.append("│").append(String.format(" %-" + maxHeaderLength + "s │", key));
            for (Long value : results.get(key)) {
                String valueStr = value != null ? String.format("%.3f", value / 1000.0) : "N/A";
                int padding = (maxHeaderLength - valueStr.length()) / 2;
                sb.append(String.format(" %" + (padding != 0 ? padding : "") + "s%-" + (maxHeaderLength - padding) + "s │", "", valueStr));
            }
            sb.append("\n");
        }
        sb.append("└").append("─".repeat(tableWidth - 2)).append("┘\n");

        return sb.toString();
    }

    public String csv() {
        StringBuilder sb = new StringBuilder();
        sb.append("Benchmark results:\n");
        sb.append(String.join(",", headers)).append("\n");
        for (String key : results.keySet()) {
            sb.append(key).append(",");
            for (Long value : results.get(key)) {
                sb.append(value).append(",");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
