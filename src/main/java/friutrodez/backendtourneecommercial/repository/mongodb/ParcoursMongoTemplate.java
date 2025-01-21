package friutrodez.backendtourneecommercial.repository.mongodb;

import friutrodez.backendtourneecommercial.model.Parcours;
import friutrodez.backendtourneecommercial.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * Service pour la gestion des parcours dans MongoDB.
 * Cette classe utilise MongoTemplate pour effectuer des opérations CRUD sur les parcours.
 * Utilise un générateur de séquence pour attribuer des identifiants uniques aux nouveaux parcours.
 *
 * @author
 * Benjamin NICOL
 * Enzo CLUZEL
 * Leïla BAUDROIT
 * Ahmed BRIBACH
 */
@Service
public class ParcoursMongoTemplate extends CustomMongoTemplate<Parcours> {

    /**
     * Constructeur de la classe ParcoursMongoTemplate.
     * Initialise la classe avec MongoTemplate et la classe Parcours.
     *
     * @param mongoTemplate le template MongoDB utilisé pour les opérations
     */
    @Autowired
    public ParcoursMongoTemplate(MongoTemplate mongoTemplate) {
        super(mongoTemplate, Parcours.class);
    }

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

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
}