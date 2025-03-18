package friutrodez.backendtourneecommercial.repository.mongodb;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.DeleteResult;
import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Template mongo personnalisé pour créer des méthodes générales à tous les types de documents mongo. <br>
 * Evite de répéter du code inutile dans la structure et évite de mettre
 * la classe de la collection à chaque appel
 * de méthode qui utilise la classe mongoTemplate.
 *
 * @param <T> le type de la collection
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
public abstract class CustomMongoTemplate<T> {

    /**
     * La collection donnée par la classe fille
     */
    protected final Class<T> collection;
    /**
     * mongoTemplate obtenue à partir du bean qui étend cette classe par le constructeur.
     */
    public MongoTemplate mongoTemplate;

    /**
     * Constructeur de la classe CustomMongoTemplate.
     *
     * @param mongoTemplate le template MongoDB utilisé pour les opérations
     * @param collection    la classe de la collection MongoDB
     */
    public CustomMongoTemplate(MongoTemplate mongoTemplate, Class<T> collection) {
        this.mongoTemplate = mongoTemplate;
        this.collection = collection;
    }

    /**
     * Trouve les documents dans la collection correspondant à la clé et la valeur spécifiées.
     *
     * @param cle    la clé de la requête
     * @param valeur la valeur de la requête
     * @return une liste de documents correspondant à la requête
     */
    public List<T> find(String cle, String valeur) {
        return mongoTemplate.find(getQuery(cle, valeur), collection);
    }

    /**
     * Enleve une entité par rapport à la clé et la valeur
     *
     * @param cle    la clé
     * @param valeur la valeur
     */
    public void removeOne(String cle, String valeur) {
        mongoTemplate.remove(findOne(cle, valeur));
    }

    public T findOne(String cle, String valeur) {
        return mongoTemplate.findOne(getQuery(cle, valeur), collection);
    }

    /*
     * Supprime tous les documents de la collection.
     */
    public void removeAll() {
        mongoTemplate.remove(new Query(), collection);
    }

    /**
     * Vérifie si un document existe dans la collection correspondant à la clé et la valeur spécifiées.
     *
     * @param cle    la clé de la requête
     * @param valeur la valeur de la requête
     * @return true si le document existe, false sinon
     */
    public boolean exists(String cle, String valeur) {
        return mongoTemplate.exists(getQuery(cle, valeur), collection);
    }

    /**
     * Crée une requête MongoDB à partir de la clé et de la valeur spécifiées.
     *
     * @param cle    la clé de la requête
     * @param valeur la valeur de la requête
     * @return la requête MongoDB
     */
    private Query getQuery(String cle, String valeur) {
        return new Query(where(cle).is(valeur));
    }

    /**
     * Récupérer des entités selon les valeurs du document reçu
     *
     * @param document le document
     * @return les documents correspondants
     */
    public List<T> getEntitiesFrom(T document) {
        ObjectMapper mapper = new ObjectMapper();
        // Il est nécessaire de ne pas inclure les null sinon rien n'est trouvé
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String entiteJson;
        try {
            entiteJson = mapper.writeValueAsString(document);
        } catch (JsonProcessingException ex) {
            throw new DonneesInvalidesException("La conversion en json n'a pas fonctionnée. Veuillez vérifier les données.");
        }
        BasicQuery basicQuery = new BasicQuery(entiteJson);
        return mongoTemplate.find(basicQuery, collection);
    }

    public DeleteResult remove(T entite) {
        return mongoTemplate.remove(entite);
    }

    /**
     * Sauvegarde un document dans la collection.
     *
     * @param object le document à sauvegarder
     */
    public void save(T object) {
        mongoTemplate.save(object);
    }

    /**
     * Sauvegarde une liste de documents dans la collection.
     *
     * @param objects la liste de documents à sauvegarder
     */
    public void saveAll(List<T> objects) {
        for (T object : objects) {
            this.save(object);
        }
    }
}