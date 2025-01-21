package friutrodez.backendtourneecommercial.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Configuration de mongo pour créer les beans d'accés à la base mongo
 */
@Configuration
public class MongoConfiguration {

    @Value("${spring.data.mongodb.uri}")
    public String mongoUri;

    @Bean
    public MongoClient mongo() {
        return MongoClients.create(mongoUri);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongo(), "mydatabase");
    }
}