package friutrodez.backendtourneecommercial.service.itineraryGenerator.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * Class representing the settings for the itinerary generator.
 * Contains configuration parameters such as the number of parallel levels.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
public class Settings {
    @Setter
    @Getter
    private static int numberOfParallelLevels = 2;
}