package friutrodez.backendtourneecommercial.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Service
public class AdresseToolsService {

    private final WebClient webClient;

    public AdresseToolsService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api-adresse.data.gouv.fr")
                .build();
    }

    private static final String API_URL = "/search/?q=";

    public boolean validateAdresse(String libelle, String codePostal, String ville) {
        // Search with libelle and as filter codePostal
        String url = API_URL + libelle + "&postcode=" + codePostal + "&city=" + ville + "&limit=1" + "&type=housenumber" + "&autocomplete=0";
        try {
            String response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            System.out.println(response);
            return parseGeoJsonResponse(response, libelle);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Double[] geolocateAdresse(String libelle, String codePostal, String ville) {
        String url = API_URL + libelle + "&postcode=" + codePostal + "&city=" + ville + "&limit=1" + "&type=housenumber" + "&autocomplete=0";
        try {
            String response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return extractCoordinates(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean parseGeoJsonResponse(String response, String libelle) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode features = root.path("features");

            for (JsonNode feature : features) {
                JsonNode properties = feature.path("properties");
                String label = properties.path("name").asText();

                if (label.equals(libelle)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Double[] extractCoordinates(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode features = root.path("features");

            if (features.isArray() && !features.isEmpty()) {
                JsonNode geometry = features.get(0).path("geometry");
                JsonNode coordinates = geometry.path("coordinates");
                return new Double[]{coordinates.get(0).asDouble(), coordinates.get(1).asDouble()};
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class Main {
    public static void main(String[] args) {
        AdresseToolsService adresseToolsService = new AdresseToolsService();
        System.out.println(adresseToolsService.validateAdresse("1 rue de la paix", "75002", "Paris"));
        System.out.println(adresseToolsService.geolocateAdresse("1 rue de la paix", "75002", "Paris"));
    }
}