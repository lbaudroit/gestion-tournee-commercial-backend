package friutrodez.backendtourneecommercial.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import friutrodez.backendtourneecommercial.model.Adresse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

/**
 * Service pour la gestion des adresses.
 *
 * Cette classe propose des méthodes pour valider une adresse
 * et géolocaliser une adresse en donnant ces coordonnées
 * @author
 * Benjamin NICOL
 * Enzo CLUZEL
 * Leïla BAUDROIT
 * Ahmed BRIBACH
 */
@Service
public class AdresseToolsService {

    private final WebClient webClient;

    public AdresseToolsService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api-adresse.data.gouv.fr")
                .build();
    }

    private static final String API_URL = "/search/?q=";

    /**
     * Cette méthode récupére les adresses à partir de l'API du gouvernement.
     * Puis elle renvoie si l'adresse est correcte.
     * @param label Le libelle de l'adresse
     * @param postCode
     * @param city
     * @return
     */
    public boolean validateAdresse(String label, String postCode, String city) {
        // Search with libelle and as filter codePostal
        String url = API_URL + label + "&postcode=" + postCode + "&city=" + city + "&limit=1" + "&type=housenumber" + "&autocomplete=0";
        try {
            String response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            System.out.println(response);
            Adresse adress = parseGeoJsonResponse(response);

            if(adress != null) {
                if (adress.getLibelle().equals(label) &&
                        adress.getVille().equals(city)&&
                        adress.getCodePostal().equals(postCode)) {
                    return true;
                } else {
                    return false;
                }
            }else {
                return false;
            }
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

    /**
     * Convertie la première réponse en adresse.
     * @param response La réponse de l'api.
     * @return Une adresse.
     */
    private Adresse parseGeoJsonResponse(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode feature = root.path("features").get(0);

            JsonNode properties = feature.path("properties");
            String label = properties.path("name").asText();
            String city = properties.path("city").asText();
            String postCode = properties.path("postcode").asText();
            return new Adresse(label,postCode,city);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
