package friutrodez.backendtourneecommercial.repository.mongodb;

import friutrodez.backendtourneecommercial.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


@Service
public class ClientMongoTemplate extends CustomMongoTemplate<Client>   {

    @Autowired
    public ClientMongoTemplate(MongoTemplate mongoTemplate) {
        super(mongoTemplate,Client.class);
    }






}
