package friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.AlgoVoyageur;

import java.util.HashMap;
import java.util.List;

public class BenchMarkResults {
    List<String> headers;

    HashMap<String, List<Integer>> results;

    public BenchMarkResults(List<String> headers) {
        this.headers = headers;
        this.results = new HashMap<>();
    }

    public void addLine(String key, List<Integer> values) {
        if (values.size() == headers.size() - 1) {
            results.put(key, values);
        } else {
            throw new IllegalArgumentException("Values size must be equal to headers size minus 1");
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

        for (AlgoVoyageur algo : AlgoVoyageur.values()) {
            String key = algo.name();
            if (results.containsKey(key)) {
                sb.append("│").append(String.format(" %-" + maxHeaderLength + "s │", key));
                for (Integer value : results.get(key)) {
                    String valueStr = String.valueOf(value);
                    int padding = (maxHeaderLength - valueStr.length()) / 2;
                    sb.append(String.format(" %" + (padding != 0 ? padding : "") + "s%-" + (maxHeaderLength - padding) + "s │", "", valueStr));
                }
                sb.append("\n");
            }
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
            for (Integer value : results.get(key)) {
                sb.append(value).append(",");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
