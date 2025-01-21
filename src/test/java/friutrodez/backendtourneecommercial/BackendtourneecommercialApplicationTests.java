package friutrodez.backendtourneecommercial;

import friutrodez.backendtourneecommercial.authentification.AuthentificationControllerTest;
import friutrodez.backendtourneecommercial.repository.ClientRepositoryTest;
import friutrodez.backendtourneecommercial.repository.UtilisateurRepositoryTest;
import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Suite
@IncludeTags({"SpringBootTest"})
@SelectClasses({ClientRepositoryTest.class, AuthentificationControllerTest.class, ConnexionTest.class, UtilisateurRepositoryTest.class})
class BackendtourneecommercialApplicationTests {

	@Test
	void contextLoads() {
		// Non utilisée
	}

}
