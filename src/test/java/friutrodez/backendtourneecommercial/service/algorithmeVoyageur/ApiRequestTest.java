package friutrodez.backendtourneecommercial.service.algorithmeVoyageur;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

class ApiRequestTest {

    @Test
    void getDistance() {
        Point point1 = new Point("tmp", 2.5732901, 44.3602355);
        Point point2 = new Point("tmp2", 3.0666963, 44.3223815);
        ApiRequest apiRequest = new ApiRequest();
        System.out.println(apiRequest.getDistance(point1, point2));
    }
}