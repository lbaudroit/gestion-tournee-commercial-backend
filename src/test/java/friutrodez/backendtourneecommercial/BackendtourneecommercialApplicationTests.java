package friutrodez.backendtourneecommercial;

import friutrodez.backendtourneecommercial.authentification.AuthenticationControllerTest;
import friutrodez.backendtourneecommercial.repository.ClientRepositoryTest;
import friutrodez.backendtourneecommercial.repository.UtilisateurRepositoryTest;
import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Suite
@IncludeTags({"SpringBootTest"})
@SelectClasses({ClientRepositoryTest.class, AuthenticationControllerTest.class, ConnexionTest.class, UtilisateurRepositoryTest.class})
class BackendtourneecommercialApplicationTests {


	@Test
	void contextLoads() {
		// Non utilisée
	}
}
