package friutrodez.backendtourneecommercial.repository.mongodb;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Template mongo personnalisé pour créer des méthodes générales à tous les types de documents mongo. <br>
 * Evite de répéter du code inutile dans la structure et évite de mettre
 * la classe de la collection à chaque appel
 * de méthode qui utilise la classe mongoTemplate.
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
    /**
     * La collection donnée par la classe fille
     */
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
        mongoTemplate.remove(getQuery(cle, valeur), collection);
    }

    /**
     * Enleve une entité par rapport à la clé et la valeur
     * @param cle la clé
     * @param valeur la valeur
     */
    public void enleverUn(String cle,String valeur) {
        mongoTemplate.remove(trouverUn(cle, valeur));
    }

    public T trouverUn(String cle, String valeur) {
        return mongoTemplate.findOne(getQuery(cle,valeur),collection);
    }

    public List<T> recupererToutesLesEntitees() {
        return  mongoTemplate.findAll(collection);
    }

    public  boolean existe(String cle,String valeur) {
        return mongoTemplate.exists(getQuery(cle, valeur), collection);
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
      
    public T sauvegarder(T object) {

        return mongoTemplate.save(object);
    }


    /**
     * Récupérer des entités selon les valeurs du document reçu
     * @param document le document
     * @return les documents correspondants
     */
    public List<T> getEntitesDepuis(T document) {
        ObjectMapper mapper = new ObjectMapper();
        // Il est nécessaire de ne pas inclure les null sinon rien n'est trouvé
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String entiteJson = "";
        try {
            entiteJson = mapper.writeValueAsString(document);
        } catch (JsonProcessingException ex) {
            throw new DonneesInvalidesException("La conversion en json n'a pas fonctionnée. Veuillez vérifier les données.");
        }
        BasicQuery basicQuery = new BasicQuery(entiteJson);
        return mongoTemplate.find(basicQuery,collection);
    }

    public DeleteResult supprimer(T entite) {
        return mongoTemplate.remove(entite);
    }

    /**
     * Modifie le document de la collection appartenant à l'id
     * @param modificationsApportees les modifications apportées
     * @param id l'id du document
     * @return le résultat de la modification
     */
    public UpdateResult modifier(T modificationsApportees, String id)  {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String entiteJson = "";
        try {
            entiteJson = mapper.writeValueAsString(modificationsApportees);
        } catch (JsonProcessingException ex) {
            throw new DonneesInvalidesException("La conversion en json n'a pas fonctionnée. Veuillez vérifier les données.");
        }
        BasicUpdate basicUpdate = new BasicUpdate(entiteJson);
        Query query = new ConstructeurQuery().ajouterConditionBasique("_id",id).build();
        return mongoTemplate.updateFirst(query,basicUpdate,collection);
    }

    public ConstructeurQuery getBuilder() {
        return new ConstructeurQuery();
    }


    public static class ConstructeurQuery {
        private final Query query;

        private Criteria critereEnCours;

        public ConstructeurQuery() {
            this.query = new Query();


        }


        public ConstructeurQuery ajouterConditionBasique(String cle, String valeur) {
            query.addCriteria(Criteria.where(cle).is(valeur));
            return this;
        }

        public ConstructeurQuery ajouterCriteria(Criteria criteria) {
            query.addCriteria(criteria);
            return this;
        }

        public Query build() {
            return query;
        }
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