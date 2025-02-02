package friutrodez.backendtourneecommercial.service;

import com.github.javafaker.Faker;
import friutrodez.backendtourneecommercial.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Service pour créer des clients rapidement.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
@Service
public class FakeDataService {

    private final Faker faker;
    private final Random random;

    // Coordinates boundaries for Aveyron region
    private static final double MIN_LAT = 43.8;
    private static final double MAX_LAT = 44.9;
    private static final double MIN_LON = 1.8;
    private static final double MAX_LON = 3.4;

    // Common Aveyron postal codes
    private static final String[] AVEYRON_POSTAL_CODES = {
            "12000", "12100", "12200", "12300", "12400", "12500",
            "12700", "12800", "12850"
    };

    // Common business types in the region
    private static final String[] BUSINESS_TYPES = {
            "Boulangerie", "Fromagerie", "Cave à vin", "Restaurant",
            "Boucherie", "Épicerie", "Café", "Hôtel", "Ferme",
            "Artisanat"
    };

    public FakeDataService() {
        this.faker = new Faker(new Locale("fr"));
        this.random = new Random();
    }

    public Client generateFakeClient() {
        // Generate random coordinates within Aveyron
        double latitude = MIN_LAT + (MAX_LAT - MIN_LAT) * random.nextDouble();
        double longitude = MIN_LON + (MAX_LON - MIN_LON) * random.nextDouble();

        String businessType = BUSINESS_TYPES[random.nextInt(BUSINESS_TYPES.length)];
        String businessName = businessType + " " + faker.name().lastName();

        return Client.builder()
                .idUtilisateur("1") // ID for Enzo
                .nomEntreprise(businessName)
                .adresse(generateFakeAdresse())
                .descriptif(generateFakeDescription(businessType))
                .coordonnees(new Coordonnees(latitude, longitude))
                .contact(generateFakeContact())
                .clientEffectif(random.nextBoolean())
                .build();
    }

    private Adresse generateFakeAdresse() {
        String postalCode = AVEYRON_POSTAL_CODES[random.nextInt(AVEYRON_POSTAL_CODES.length)];
        return new Adresse(
                faker.address().streetAddress(),
                postalCode,
                faker.address().city()
        );
    }

    private String generateFakeDescription(String businessType) {
        String[] descriptions = {
                "Établissement réputé pour sa qualité de service et ses produits locaux.",
                "Une institution locale depuis plus de " + (10 + random.nextInt(40)) + " ans.",
                "Entreprise familiale proposant des produits traditionnels de l'Aveyron.",
                "Commerce de proximité apprécié pour son authenticité.",
                "Établissement moderne alliant tradition et innovation."
        };
        return descriptions[random.nextInt(descriptions.length)];
    }

    private Contact generateFakeContact() {
        return new Contact(
                faker.name().lastName(),
                faker.name().firstName(),
                "05" + faker.number().numberBetween(65000000, 65999999)
        );
    }

    public List<Client> generateFakeClients(int count) {
        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            clients.add(generateFakeClient());
        }
        return clients;
    }
}