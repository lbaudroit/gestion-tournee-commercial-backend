package friutrodez.backendtourneecommercial;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootApplication
public class BackendtourneecommercialApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendtourneecommercialApplication.class, args);
	}

}
