package friutrodez.backendtourneecommercial;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootTest
public class ConnexionTest {
    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;
    @Test
    void ConnexionMySQLTest() throws SQLException {

        Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydatabase", "myuser", "secret");
        Assert.assertNotNull(conn);
    }

    @Test

    void ConnexionMongoDBTest() {
        MongoClient mongoClient = MongoClients.create(mongoUri);
        Assert.assertNotNull(mongoClient);
    }
}
