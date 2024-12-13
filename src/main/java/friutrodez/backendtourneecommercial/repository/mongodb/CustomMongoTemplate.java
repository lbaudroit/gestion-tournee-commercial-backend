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
 * de méthode qui utilise la classe mongoTemplate
 * @param <T> le type de la collection
 */
public abstract class CustomMongoTemplate<T>  {

    /**
     * mongoTemplate obtenue à partir du bean qui étend cette classe par le constructeur
     */
    public MongoTemplate mongoTemplate;
    /**
     * Collection de mongoDB
     */
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
        return mongoTemplate.exists(getQuery(cle, valeur),collection);
    }
    private Query getQuery(String cle, String valeur) {
        return new Query(where(cle).is(valeur));
    }
    public T sauvegarder(T object) {

        return mongoTemplate.save(object);
    }


    /**
     * Récupérer des clients selon les valeurs du document reçus
     * @param document
     * @return les documents correspondant
     */
    public List<T> getClientSpecifique(T document) {
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
     * @return le resultat de la modification
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


    class  ConstructeurQuery {
        private Query query;

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

        public Query build(){
            return query;
        }
    }
}

