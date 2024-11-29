package friutrodez.backendtourneecommercial.repository.mongodb;

import friutrodez.backendtourneecommercial.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class ClientRepository   {
    @Autowired
    public MongoTemplate mongoTemplate;
    public <T> List<T> trouverPar(String cle, String valeur, Class<T> collection) {
        return  mongoTemplate.find(getQuery(cle,valeur),collection);
    }
    public <T> void enlever(String cle,String valeur,Class<T> collection) {
        //TODO : gérer le cas ou la collection n'existe pas
        mongoTemplate.remove(getQuery(cle,valeur),collection);
    }

    public <T> boolean existe(String cle,String valeur,Class<T> collection) {
        return mongoTemplate.exists(getQuery(cle, valeur),collection);
    }
    private Query getQuery(String cle,String valeur) {
        return new Query(where(cle).is(valeur));
    }
}
