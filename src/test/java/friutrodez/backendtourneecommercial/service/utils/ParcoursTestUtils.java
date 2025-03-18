package friutrodez.backendtourneecommercial.service.utils;

import com.github.javafaker.Faker;
import friutrodez.backendtourneecommercial.dto.ParcoursDTO;
import friutrodez.backendtourneecommercial.model.Coordonnees;
import friutrodez.backendtourneecommercial.model.EtapesParcours;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonLineString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParcoursTestUtils {
    private static final Faker faker = new Faker();

    /**
     * Génère un parcours avec des données entièrement aléatoires, y compris les étapes,
     * le nom, le tracé géographique et les horaires.
     *
     * @return un objet ParcoursDTO représentant un parcours généré de manière aléatoire.
     */
    public static ParcoursDTO createRandomParcours() {
        List<EtapesParcours> etapes = IntStream.range(0, 5).mapToObj(i ->
                new EtapesParcours(faker.address().city(), faker.bool().bool(), new Coordonnees(
                        Double.parseDouble(faker.address().latitude().replace(',','.'))
                        , Double.parseDouble(faker.address().longitude().replace(',','.'))))
        ).collect(Collectors.toList());

        return new ParcoursDTO(
                etapes,
                faker.commerce().productName(),
                new GeoJsonLineString(List.of(new Point(faker.number().randomDouble(6, -180, 180), faker.number().randomDouble(6, -90, 90)))),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1)
        );
    }
}
