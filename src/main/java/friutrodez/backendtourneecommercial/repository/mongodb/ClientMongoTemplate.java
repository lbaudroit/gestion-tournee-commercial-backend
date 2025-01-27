package friutrodez.backendtourneecommercial.repository.mongodb;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.DeleteResult;
import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.model.Adresse;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import friutrodez.backendtourneecommercial.service.SequenceGeneratorService;
import org.springframework.data.domain.Pageable;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;


/**
 * Service pour la gestion des clients dans MongoDB.
 * Cette classe utilise MongoTemplate pour effectuer des opérations CRUD sur les clients.
 * Utilise un générateur de séquence pour attribuer des identifiants uniques aux nouveaux clients.
 *
 * @author
 * Benjamin NICOL
 * Enzo CLUZEL
 * Leïla BAUDROIT
 * Ahmed BRIBACH
 */
@Service
public class ClientMongoTemplate extends CustomMongoTemplate<Client> {

    /**
     * Constructeur de la classe ClientMongoTemplate.
     * Initialise la classe avec MongoTemplate et la classe Client.
     *
     * @param mongoTemplate le template MongoDB utilisé pour les opérations
     */
    @Autowired
    public ClientMongoTemplate(MongoTemplate mongoTemplate) {
        super(mongoTemplate, Client.class);
    }

    public List<Client> getAllClients(String idUser) {
        Query query = new Query().addCriteria(where("idUtilisateur").is(idUser));
        return  mongoTemplate.find(query,collection);

    }

    public List<Client> getClientsByPage(String idUser, Pageable page) {
        // Création d'un objet Pageable pour définir la pagination

        // Ajout des critères de recherche
        Query query = new Query().addCriteria(Criteria.where("idUtilisateur").is(idUser));
        query.with(page); // Appliquer la pagination au Query

        // Récupération des clients paginés
        return mongoTemplate.find(query, collection);
    }
    /**
     * Retire de la BD, le client avec l'id et l'idUtilisateur correspondants
     * @param idClient
     * @param idUser
     * @return le resultat de la suppression
     */
    public DeleteResult removeClientsWithId(String idClient,String idUser) {
        Query query = new Query().addCriteria(where("idUtilisateur").is(idUser))
                .addCriteria(where("_id").is(idClient));
        return mongoTemplate.remove(query,collection);
    }


    /**
     * Vérifie si le client existe dans la BD à partir de l'utilisateur et de son adresse
     * @param idUser l'id de l'utilisateur
     * @param informations l'adresse du client à vérifier
     * @return true si le client existe false sinon
     */
    public boolean exists(String idUser, Adresse informations) {
        ObjectMapper mapper = new ObjectMapper();
        // Il est nécessaire de ne pas inclure les null sinon rien n'est trouvé
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String adresseJson = "";
        try {
            adresseJson = mapper.writeValueAsString(informations);
        } catch (JsonProcessingException ex) {
            throw new DonneesInvalidesException("La conversion en json n'a pas fonctionnée. Veuillez vérifier les données.");
        }
        BasicQuery basicQuery = (BasicQuery) new BasicQuery(adresseJson).addCriteria(where("idUtilisateur").is(idUser));
        return mongoTemplate.exists(basicQuery,collection);
    }

    public Client getOneClient(String idClient,String idUser) {
        Query query = new Query().addCriteria(where("idUtilisateur").is(idUser))
                .addCriteria(where("_id").is(idClient));
        System.out.println(query.toString());
        return  mongoTemplate.findOne(query,collection);
    }

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    /**
     * Sauvegarde un client dans la base de données.
     * Si l'identifiant du client est nul, un nouvel identifiant est généré.
     *
     * @param client le client à sauvegarder
     */
    @Override
    public void save(Client client) {
        if (client.get_id() == null) {
            client.set_id(sequenceGeneratorService.generateSequence(Client.SEQUENCE_NAME));
        }
        mongoTemplate.save(client);
    }
    public int getNumberClients(Utilisateur idUser){
        Query query = new Query().addCriteria(where("idUtilisateur").is(idUser));
        long totalElements = mongoTemplate.count(query, collection);

        // Calcul du nombre total de pages
        int totalPages = (int) Math.ceil((double) totalElements / 30);
        return totalPages;
    }
}
