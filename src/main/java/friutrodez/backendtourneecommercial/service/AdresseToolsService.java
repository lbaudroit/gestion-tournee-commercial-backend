package friutrodez.backendtourneecommercial.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import friutrodez.backendtourneecommercial.model.Adresse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

/**
 * Service pour la gestion des adresses.
 * <p>
 * Cette classe propose des méthodes pour valider une adresse
 * et géolocaliser une adresse en donnant ces coordonnées.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
@Service
public class AdresseToolsService {

    private static final Logger log = LoggerFactory.getLogger(AdresseToolsService.class);
    private final WebClient webClient;

    public AdresseToolsService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api-adresse.data.gouv.fr")
                .build();
    }

    private static final String API_URL = "/search/";

    /**
     * Cette méthode récupère les adresses à partir de l'API du gouvernement.
     * Puis, elle renvoie si l'adresse est correcte.
     *
     * @param label    Le libelle de l'adresse.
     * @param postCode Le code postal de l'adresse.
     * @param city     La ville de l'adresse.
     * @return         True si l'adresse correspond à celle donnée en paramètre, sinon false.
     */
    public boolean validateAdresse(String label, String postCode, String city) {
        // Search with libelle and as filter codePostal
        String url = API_URL
                + "?q=" + label
                + "&postcode=" + postCode
                + "&city=" + city
                + "&limit=1"
                + "&type=housenumber"
                + "&autocomplete=0";
        try {
            String response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            System.out.println(response);
            Adresse address = parseGeoJsonResponse(response);

            if (address != null) {
                return address.getLibelle().equals(label) &&
                        address.getVille().equals(city) &&
                        address.getCodePostal().equals(postCode);
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error(e.toString());
            return false;
        }
    }

    public Double[] geolocateAdresse(String libelle, String codePostal, String ville) {
        String url = API_URL
                + "?q=" + libelle
                + "&postcode=" + codePostal
                + "&city=" + ville
                + "&limit=1"
                + "&type=housenumber"
                + "&autocomplete=0";
        try {
            String response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return extractCoordinates(response);
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }

    /**
     * Convertie la première réponse en adresse.
     *
     * @param response La réponse de l'api.
     * @return         Une adresse.
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
            return new Adresse(label, postCode, city);

        } catch (IOException e) {
            log.error(e.toString());
        }
        return null;
    }

    /**
     * Extrait les coordonnées de l'adresse.
     *
     * @param response La réponse de l'api
     * @return         Un tableau avec les coordonnées.
     */
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
            log.error(e.toString());
        }
        return null;
    }
}
