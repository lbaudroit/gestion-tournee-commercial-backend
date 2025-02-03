package friutrodez.backendtourneecommercial.service.algorithmeVoyageur;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

public class ApiRequest {
    private WebClient webClient;

    private final static String API_URL = "https://data.geopf.fr/navigation/itineraire?start=%s&end=%s&profile=car&optimization=shortest&getSteps=false&getBbox=false&distanceUnit=meter&resource=bdtopo-osrm";

    public ApiRequest() {
        this.webClient = WebClient.builder()
                .baseUrl("https://data.geopf.fr")
                .build();
    }

    public double getDistance(Point start, Point end) {
        String url = buildUrl(start, end);
        System.out.println(url);
        String response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return parseResponse(response);
    }

    private String buildUrl(Point start, Point end) {
        String startStr = start.getLongitude() + "," + start.getLatitude();
        String endStr = end.getLongitude() + "," + end.getLatitude();
        return String.format(API_URL, startStr, endStr);
    }

    private double parseResponse(String response) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(response).get("distance").asInt();
        } catch (Exception e) {
            return -1;
        }
    }

}
