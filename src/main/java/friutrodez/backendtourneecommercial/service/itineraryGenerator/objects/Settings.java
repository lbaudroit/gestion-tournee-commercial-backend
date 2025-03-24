package friutrodez.backendtourneecommercial.service.itineraryGenerator.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * Class representing the settings for the itinerary generator.
 * Contains configuration parameters such as the number of parallel levels.
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
public class Settings {
    @Setter
    @Getter
    private static int numberOfParallelLevels = 2;
}