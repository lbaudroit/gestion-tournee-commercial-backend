package friutrodez.backendtourneecommercial.repository.mongodb;

import friutrodez.backendtourneecommercial.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class ClientMongoTemplate extends CustomMongoTemplate<Client>   {

    @Autowired
    public ClientMongoTemplate(MongoTemplate mongoTemplate) {
        //TODO : gérer le cas ou la collection n'existe pas
        super(mongoTemplate,Client.class);
    }
}
