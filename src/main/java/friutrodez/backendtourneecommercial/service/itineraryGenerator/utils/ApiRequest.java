package friutrodez.backendtourneecommercial.service.itineraryGenerator.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class ApiRequest {
    private WebClient webClient;

    private final static String API_URL = "/v2/matrix/driving-car";

    public ApiRequest() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openrouteservice.org")
                .defaultHeader("Authorization", "5b3ce3597851110001cf6248341d92c562084883bfb4644b9ca11e03")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public void createMatrix(List<Point> points) {
        String response = webClient.post()
                .uri(API_URL)
                .bodyValue(parseMatrix(points))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        parseResponse(response, points);
    }

    private String parseMatrix(List<Point> points) {
       String body = "{\"locations\":[";
       for (Point point : points) {
              body += "[" + point.getLongitude() + "," + point.getLatitude() + "],";
       }
       body = body.substring(0, body.length() - 1);
       body += "],\"metrics\":[\"distance\"]}";
         return body;
    }

    private void parseResponse(String response, List<Point> points) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(response);
            JsonNode distancesNode = rootNode.path("distances");

            for (int i = 0; i < points.size(); i++) {
                JsonNode distanceArray = distancesNode.get(i);
                Point point = points.get(i);

                for (int j = 0; j < points.size(); j++) {
                    if (i != j) {
                        Point destination = points.get(j);
                        int distance = distanceArray.get(j).asInt();
                        point.getDistances().put(destination, distance);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
