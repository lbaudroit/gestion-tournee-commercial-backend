package friutrodez.backendtourneecommercial.controlleur;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.x.protobuf.MysqlxExpr;
import friutrodez.backendtourneecommercial.controller.ClientControlleur;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class ClientControlleurTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ClientControlleur clientControlleur;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ClientMongoTemplate clientMongoTemplate;

    @Test
    void creationClientTest() throws Exception {

        clientMongoTemplate.enlever("nomEntreprise","entrepriseTest");
        Client client = new Client();
        client.setNomEntreprise("entrepriseTest");
        client.setIdUtilisateur("1");

         String jsonClient= objectMapper.writeValueAsString(client);
        mockMvc.perform(put("/client/creer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonClient))
                .andExpect(status().isOk());

        //Assertions.a
        clientMongoTemplate.enlever("nomEntreprise","entrepriseTest");
    }

}
