package friutrodez.backendtourneecommercial.repository;

import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mysql.UtilisateurRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.sql.*;
import java.util.Optional;

@SpringBootTest

class UtilisateurRepositoryTest {

    @Autowired
    UtilisateurRepository utilisateurRepository;


    @Test
    void testUserCOnnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydatabase", "myuser", "secret");
        System.out.println(conn.getSchema());
        PreparedStatement statement = conn.prepareStatement("Select * FROM utilisateur where id = ?");
        statement.setInt(1,1);
        ResultSet resultSet =statement.executeQuery();

    }
    @Test
    void testCreation() throws SQLException {
        Optional<Utilisateur> utilisateurTrouve = utilisateurRepository.findById(1L);
        if(utilisateurTrouve.isPresent()) {
            utilisateurRepository.deleteById(1L);
        }
        Utilisateur utilisateur = new Utilisateur();
       // Attention ne pas utiliser id sinon ça sauvegarde au lieu de créer : Causé peut être par @GeneratedValue
        //utilisateur.setId(1L);
        utilisateur.setNom("en");
        utilisateur.setPrenom("quelquechose");
        utilisateur.setCodePostal("zef");
        utilisateurRepository.save(utilisateur);


         utilisateurTrouve = utilisateurRepository.findById(1L);
        Assert.isTrue(utilisateurTrouve.isPresent(),"L'utilisateur n'a pas été trouvé");

    }

}