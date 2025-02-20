package friutrodez.backendtourneecommercial.service.itineraryGenerator.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * Classe pour effectuer des requêtes API pour générer des matrices de distances.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
public class ApiRequest {
    private final WebClient webClient;
    private static final String API_URL = "/v2/matrix/driving-car";

    /**
     * Constructeur qui initialise le client Web avec les en-têtes nécessaires.
     */
    public ApiRequest() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openrouteservice.org")
                .defaultHeader("Authorization", "5b3ce3597851110001cf6248341d92c562084883bfb4644b9ca11e03")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    /**
     * Crée une matrice de distances pour une liste de points.
     *
     * @param points Liste de points pour lesquels générer la matrice.
     */
    public void createMatrix(List<Point> points) {
        String response = webClient.post()
                .uri(API_URL)
                .bodyValue(pointsToMatrix(points))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        parseResponse(response, points);
    }

    /**
     * Génère le corps de la requête pour la matrice de distances.
     *
     * @param points Liste de points à inclure dans la matrice.
     * @return Chaîne JSON représentant le corps de la requête.
     */
    private String pointsToMatrix(List<Point> points) {
        StringBuilder body = new StringBuilder("{\"locations\":[");
        for (Point point : points) {
            body.append("[").append(point.getLongitude()).append(",").append(point.getLatitude()).append("],");
        }
        body.setLength(body.length() - 1); // Supprime la dernière virgule
        body.append("],\"metrics\":[\"distance\"]}");
        return body.toString();
    }

    /**
     * Analyse la réponse de l'API et met à jour les points avec les distances.
     *
     * @param response Réponse JSON de l'API.
     * @param points   Liste de points à mettre à jour avec les distances.
     */
    private void parseResponse(String response, List<Point> points) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(response);
            JsonNode distancesNode = rootNode.path("distances");
            updatePointsWithDistances(distancesNode, points);
        } catch (Exception e) {
            System.out.println("Error while parsing the response: " + e.getMessage());
        }
    }

    /**
     * Met à jour les points avec les distances à partir du nœud JSON des distances.
     *
     * @param distancesNode Nœud JSON contenant les distances.
     * @param points        Liste de points à mettre à jour.
     */
    private void updatePointsWithDistances(JsonNode distancesNode, List<Point> points) {
        for (int i = 0; i < points.size(); i++) {
            JsonNode distanceArray = distancesNode.get(i);
            Point point = points.get(i);
            updatePointDistances(point, distanceArray, points);
        }
    }

    /**
     * Met à jour un point avec les distances vers les autres points.
     *
     * @param point         Point à mettre à jour.
     * @param distanceArray Tableau JSON des distances pour ce point.
     * @param points        Liste de tous les points.
     */
    private void updatePointDistances(Point point, JsonNode distanceArray, List<Point> points) {
        for (int j = 0; j < points.size(); j++) {
            if (point != points.get(j)) {
                Point destination = points.get(j);
                int distance = distanceArray.get(j).asInt();
                point.getDistances().put(destination, distance);
            }
        }
    }
}