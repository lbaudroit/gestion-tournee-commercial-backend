package friutrodez.backendtourneecommercial.repository.mongodb;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Template mongo personnalisé pour créer des méthodes générales à tous les types de documents mongo. <br>
 * Evite de répéter du code inutile dans la structure et évite de mettre
 * la classe de la collection à chaque appel 
 * de méthode qui utilise la classe mongoTemplate
 * @param <T> le type de la collection
 */
public abstract class CustomMongoTemplate<T>  {

    /**
     * mongoTemplate obtenue à partir du bean qui étend cette classe par le constructeur
     */
    public MongoTemplate mongoTemplate;
    protected final Class<T> collection;

    public CustomMongoTemplate(MongoTemplate mongoTemplate,Class<T> collection){
        this.mongoTemplate = mongoTemplate;
        this.collection = collection;
    }
    public  List<T> trouverPar(String cle, String valeur) {
        return  mongoTemplate.find(getQuery(cle,valeur),collection);
    }
    public void enlever(String cle,String valeur) {
        //TODO : gérer le cas ou la collection n'existe pas
        mongoTemplate.remove(getQuery(cle,valeur),collection);
    }

    public List<T> recupererToutesLesEntites() {
        return  mongoTemplate.findAll(collection);
    }

    public  boolean existe(String cle,String valeur) {
        return mongoTemplate.exists(getQuery(cle, valeur),collection);
    }
    private Query getQuery(String cle, String valeur) {
        return new Query(where(cle).is(valeur));
    }
    public T sauvegarder(T object) {
        return mongoTemplate.save(object);
    }
}

