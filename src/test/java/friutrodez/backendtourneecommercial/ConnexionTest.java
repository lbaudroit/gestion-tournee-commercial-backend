package friutrodez.backendtourneecommercial;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Classe de test pour tester si la connexion aux bd dans le docker fonctionnent.
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
@SpringBootTest
public class ConnexionTest {
    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;
    @Value("${spring.datasource.url}")
    private String mySQLURL;
    @Value("${spring.datasource.username}")
    private String userMySQL;
    @Value("${spring.datasource.password}")
    private String passwordMySQL;

    @Test
    void testConnectionMySQL() throws SQLException {
        Connection conn = DriverManager.getConnection(mySQLURL, userMySQL, passwordMySQL);
        Assertions.assertNotNull(conn);
    }

    @Test
    void testConnectionMongoDB() {
        MongoClient mongoClient = MongoClients.create(mongoUri);
        Assertions.assertNotNull(mongoClient);
    }
}
