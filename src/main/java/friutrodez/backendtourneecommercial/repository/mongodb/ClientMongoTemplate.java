package friutrodez.backendtourneecommercial.repository.mongodb;

import com.mongodb.client.result.DeleteResult;
import friutrodez.backendtourneecommercial.model.Client;
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
 * Service pour la gestion des clients dans MongoDB.
 * Cette classe utilise MongoTemplate pour effectuer des opérations CRUD sur les clients.
 * Utilise un générateur de séquence pour attribuer des identifiants uniques aux nouveaux clients.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
@Service
public class ClientMongoTemplate extends CustomMongoTemplate<Client> {

    private final SequenceGeneratorService sequenceGeneratorService;

    private static final int PAGE_SIZE = 30;

    /**
     * Constructeur de la classe ClientMongoTemplate.
     * Initialise la classe avec MongoTemplate et la classe Client.
     *
     * @param mongoTemplate le template MongoDB utilisé pour les opérations
     */
    @Autowired
    public ClientMongoTemplate(MongoTemplate mongoTemplate, SequenceGeneratorService sequenceGeneratorService) {
        super(mongoTemplate, Client.class);
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    /**
     * Récupère tous les clients associés à un utilisateur donné.
     *
     * @param idUser Identifiant de l'utilisateur dont on veut récupérer tous les clients.
     * @return Une liste de clients appartenant à l'utilisateur spécifié.
     * @throws IllegalArgumentException si l'idUser est null ou vide.
     */
    public List<Client> getAllClients(String idUser) {
        Query query = new Query().addCriteria(where("idUtilisateur").is(idUser));
        return mongoTemplate.find(query, collection);

    }

    /**
     * Récupère une liste paginée de clients associés à un utilisateur donné.
     *
     * @param idUser l'identifiant de l'utilisateur dont on souhaite récupérer les clients
     * @param page   la pagination désirée pour cette recherche
     * @return Une liste paginée de clients appartenant à l'utilisateur.
     * @throws IllegalArgumentException si l'idUser est null ou vide.
     */
    public List<Client> getClientsByPage(String idUser, Pageable page) {
        // Ajout des critères de recherche
        Query query = new Query().addCriteria(Criteria.where("idUtilisateur").is(idUser));
        query.with(page); // Appliquer la pagination au Query

        // Récupération des clients paginés
        return mongoTemplate.find(query, collection);
    }

    /**
     * Retire de la BD, le client avec l'id et l'idUtilisateur correspondants
     *
     * @param idClient l'identifiant du client à supprimer
     * @param idUser   l'identifiant de l'utilisateur possédant ce client
     * @return le resultat de la suppression
     */
    public DeleteResult removeClientsWithId(String idClient, String idUser) {
        Query query = new Query().addCriteria(where("idUtilisateur").is(idUser))
                .addCriteria(where("_id").is(idClient));
        return mongoTemplate.remove(query, collection);
    }


    /**
     * Récupère un client spécifique appartenant à un utilisateur donné.
     *
     * @param idClient Identifiant unique du client à récupérer.
     * @param idUser   Identifiant de l'utilisateur propriétaire du client.
     * @return Le client correspondant aux critères, ou null s'il n'est pas trouvé.
     * @throws IllegalArgumentException si l'un des identifiants est null ou vide.
     */
    public Client getOneClient(String idClient, String idUser) {
        Query query = new Query().addCriteria(where("idUtilisateur").is(idUser))
                .addCriteria(where("_id").is(idClient));
        System.out.println(query);
        return mongoTemplate.findOne(query, collection);
    }

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

    /**
     * Calcule le nombre total de pages pour les clients d'un utilisateur donné.
     *
     * @param idUser Identifiant de l'utilisateur dont on veut compter les clients.
     * @return Le nombre total de pages en fonction de la taille de page définie.
     * @throws IllegalArgumentException si l'idUser est null ou vide.
     */
    public int getPageCountForUser(String idUser) {
        Query query = new Query().addCriteria(where("idUtilisateur").is(idUser));
        long totalElements = mongoTemplate.count(query, collection);
        // Calcul du nombre total de pages
        return (int) Math.ceil((double) totalElements / PAGE_SIZE);
    }
}
