package friutrodez.backendtourneecommercial.repository.mongodb;

import friutrodez.backendtourneecommercial.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import friutrodez.backendtourneecommercial.service.SequenceGeneratorService;

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
}
