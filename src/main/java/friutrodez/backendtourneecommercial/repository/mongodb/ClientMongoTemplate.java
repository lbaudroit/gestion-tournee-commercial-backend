package friutrodez.backendtourneecommercial.repository.mongodb;

import friutrodez.backendtourneecommercial.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
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
        super(mongoTemplate,Client.class);
    }

    public List<Client> getClientSpecifique(Client client) {
        ConstructeurQuery constructeurQuery =getBuilder();
        if(client.get_id() != null) {
            constructeurQuery.ajouterConditionBasique("_id",client.get_id());
        }
        if(client.getNomEntreprise() != null) {
            constructeurQuery.ajouterConditionBasique("nomEntreprise",client.getNomEntreprise());
        }
        if(client.getCoordonnees() != null) {
            constructeurQuery.ajouterCriteria(Criteria.where("coordonnees").
                    all(client.getCoordonnees().latitude()
                            ,client.getCoordonnees().longitude()));
        }
        return mongoTemplate.find(constructeurQuery.build(), collection);
    }

}
