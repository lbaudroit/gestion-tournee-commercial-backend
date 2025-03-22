package friutrodez.backendtourneecommercial.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.lang.NonNull;

/**
 * Classe de configuration pour MongoDB.
 * Cette classe configure la connexion à la base de données MongoDB et crée un bean MongoTemplate.
 * Lorsque cette classe étend la classe AbstractMongoClientConfiguration
 * les index sur les documents mongoDB sont créés automatiquement
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
@Configuration
@EnableMongoRepositories(basePackages = "friutrodez.backendtourneecommercial.repository")
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    public String mongoUri;

    /**
     * Crée un bean MongoClient pour se connecter à MongoDB.
     *
     * @return MongoClient
     */
    @Override
    @Bean
    @NonNull
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUri);
    }

    /**
     * Crée un bean MongoTemplate pour interagir avec MongoDB.
     *
     * @return MongoTemplate
     */
    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), "mydatabase");
    }

    /**
     * Récupère le nom de la base de données.
     *
     * @return Le nom de la base de données.
     */
    @Override
    @NonNull
    protected String getDatabaseName() {
        return "mydatabase";
    }

    /**
     * Active la création automatique des index sur les documents MongoDB.
     *
     * @return true
     */
    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
}