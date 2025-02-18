package friutrodez.backendtourneecommercial.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Classe de configuration pour MongoDB.
 * Cette classe configure la connexion à la base de données MongoDB et crée un bean MongoTemplate.
 *
 * Lorsque cette classe étend la classe AbstractMongoClientConfiguration
 * les index sur les documents mongoDB sont créés automatiquement
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
@Configuration
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    public String mongoUri;

    /**
     * Crée un bean MongoClient pour se connecter à MongoDB.
     *
     * @return MongoClient
     */
    @Bean
    public MongoClient mongo() {
        return MongoClients.create(mongoUri);
    }

    /**
     * Crée un bean MongoTemplate pour interagir avec MongoDB.
     *
     * @return MongoTemplate
     */
    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongo(), "mydatabase");
    }


    @Override
    protected String getDatabaseName() {
        return "mydatabase";
    }

}