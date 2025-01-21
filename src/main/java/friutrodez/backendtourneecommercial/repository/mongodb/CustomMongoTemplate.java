package friutrodez.backendtourneecommercial.repository.mongodb;

import org.springframework.data.mongodb.core.MongoTemplate;
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
 *
 * @author
 * Benjamin NICOL
 * Enzo CLUZEL
 * Leïla BAUDROIT
 * Ahmed BRIBACH
 */
public abstract class CustomMongoTemplate<T>  {

    /**
     * mongoTemplate obtenue à partir du bean qui étend cette classe par le constructeur.
     */
    public MongoTemplate mongoTemplate;
    private final Class<T> collection;

    /**
     * Constructeur de la classe CustomMongoTemplate.
     *
     * @param mongoTemplate le template MongoDB utilisé pour les opérations
     * @param collection la classe de la collection MongoDB
     */
    public CustomMongoTemplate(MongoTemplate mongoTemplate, Class<T> collection){
        this.mongoTemplate = mongoTemplate;
        this.collection = collection;
    }

    /**
     * Trouve les documents dans la collection correspondant à la clé et la valeur spécifiées.
     *
     * @param cle la clé de la requête
     * @param valeur la valeur de la requête
     * @return une liste de documents correspondant à la requête
     */
    public List<T> find(String cle, String valeur) {
        return mongoTemplate.find(getQuery(cle, valeur), collection);
    }

    /**
     * Supprime les documents dans la collection correspondant à la clé et la valeur spécifiées.
     *
     * @param cle la clé de la requête
     * @param valeur la valeur de la requête
     */
    public void remove(String cle, String valeur) {
        //TODO : gérer le cas ou la collection n'existe pas
        mongoTemplate.remove(getQuery(cle, valeur), collection);
    }

    /**
     * Supprime tous les documents de la collection.
     */
    public void removeAll() {
        mongoTemplate.remove(new Query(), collection);
    }

    /**
     * Vérifie si un document existe dans la collection correspondant à la clé et la valeur spécifiées.
     *
     * @param cle la clé de la requête
     * @param valeur la valeur de la requête
     * @return true si le document existe, false sinon
     */
    public boolean exists(String cle, String valeur) {
        return mongoTemplate.exists(getQuery(cle, valeur), collection);
    }

    /**
     * Crée une requête MongoDB à partir de la clé et de la valeur spécifiées.
     *
     * @param cle la clé de la requête
     * @param valeur la valeur de la requête
     * @return la requête MongoDB
     */
    private Query getQuery(String cle, String valeur) {
        return new Query(where(cle).is(valeur));
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