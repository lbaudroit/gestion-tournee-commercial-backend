package friutrodez.backendtourneecommercial.repository;

import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mysql.UtilisateurRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.sql.SQLException;
import java.util.Optional;

@SpringBootTest
public class UtilisateurRepositoryTest {

    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Test
    @Transactional
    @Rollback
    void testCreation() throws SQLException {

        Utilisateur utilisateur = Utilisateur.builder()
                .nom("en")
                .prenom("sdg")
                .telephone("0643454789")
                .motDePasse("Lehhh_123")
                .libelleAdresse("2 Rue d'Arpajon")
                .codePostal("13450")
                .ville("Sévéra")
                .latitude(44.3223815F)
                .longitude(3.0666963F)
                .build();

        utilisateurRepository.save(utilisateur);

        Utilisateur utilisateurTrouve = utilisateurRepository.findByNom("en");
        Assertions.assertNotNull(utilisateurTrouve,"L'utilisateur n'a pas été trouvé");
        utilisateurRepository.delete(utilisateur);
    }

}