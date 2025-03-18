package friutrodez.backendtourneecommercial.repository.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.result.DeleteResult;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Parcours;
import friutrodez.backendtourneecommercial.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Service pour la gestion des parcours dans MongoDB.
 * Cette classe utilise MongoTemplate pour effectuer des opérations CRUD sur les parcours.
 * Utilise un générateur de séquence pour attribuer des identifiants uniques aux nouveaux parcours.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
@Service
public class ParcoursMongoTemplate extends CustomMongoTemplate<Parcours> {

    private final SequenceGeneratorService sequenceGeneratorService;

    /**
     * Constructeur de la classe ParcoursMongoTemplate.
     * Initialise la classe avec MongoTemplate et la classe Parcours.
     *
     * @param mongoTemplate le template MongoDB utilisé pour les opérations
     * @param sequenceGeneratorService le service de génération de séquence pour les identifiants uniques
     * @param mongo le client MongoDB
     */
    @Autowired
    public ParcoursMongoTemplate(MongoTemplate mongoTemplate, SequenceGeneratorService sequenceGeneratorService, MongoClient mongo) {
        super(mongoTemplate, Parcours.class);
        this.sequenceGeneratorService = sequenceGeneratorService;
    }


    /**
     * Sauvegarde un parcours dans la base de données.
     * Si l'identifiant du parcours est nul, un nouvel identifiant est généré.
     *
     * @param parcours le parcours à sauvegarder
     */
    public void save(Parcours parcours) {
        if (parcours.get_id() == null) {
            parcours.set_id(sequenceGeneratorService.generateSequence(Parcours.SEQUENCE_NAME));
        }
        mongoTemplate.save(parcours);
    }

    /**
     * Calcule le nombre de pages pour un utilisateur donné en fonction de la taille de la page.
     *
     * @param idUser l'identifiant de l'utilisateur
     * @param pageSize la taille de la page
     * @return le nombre de pages
     */
    public int getPageCountForUser(String idUser, int pageSize) {
        Query query = new Query().addCriteria(where("idUtilisateur").is(idUser));
        long totalElements = mongoTemplate.count(query, collection);
        return (int) Math.ceil((double) totalElements / pageSize);
    }

    /**
     * Récupère une liste paginée de clients associés à un utilisateur donné.
     *
     * @param idUser l'identifiant de l'utilisateur dont on souhaite récupérer les clients
     * @param page   la pagination désirée pour cette recherche
     * @return Une liste paginée de clients appartenant à l'utilisateur.
     * @throws IllegalArgumentException si l'idUser est null ou vide.
     */
    public List<Parcours> getParcoursByPage(String idUser, Pageable page) {
        Query query = new Query().addCriteria(Criteria.where("idUtilisateur").is(idUser));
        query.with(page);
        query.fields().include("nom");
        query.fields().include("dateDebut");
        query.fields().include("dateFin");


        // Récupération des clients paginés
        return mongoTemplate.find(query, collection);
    }

    /**
     * Retire de la BD, le parcours avec l'id et l'idUtilisateur correspondants
     *
     * @param idParcours l'identifiant du parcours à supprimer
     * @param idUser   l'identifiant de l'utilisateur possédant ce client
     * @return le resultat de la suppression
     */
    public DeleteResult removeParcoursWithID(String idParcours, String idUser) {
        Query query = new Query().addCriteria(where("idUtilisateur").is(idUser))
                .addCriteria(where("_id").is(idParcours));
        return mongoTemplate.remove(query, collection);
    }
}