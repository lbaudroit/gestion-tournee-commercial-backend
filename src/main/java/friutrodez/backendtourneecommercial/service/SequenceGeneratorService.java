package friutrodez.backendtourneecommercial.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import friutrodez.backendtourneecommercial.model.MongoDBMysqlSequence;

/**
 * Service pour la génération de séquences dans MongoDB.
 * Utilise MongoOperations pour effectuer des opérations de modification et de mise à jour sur les séquences.
 * Permet de générer des identifiants uniques pour les documents MongoDB.
 *
 * @author
 * Benjamin NICOL
 * Enzo CLUZEL
 * Leïla BAUDROIT
 * Ahmed BRIBACH
 */
@Service
public class SequenceGeneratorService {

    private final MongoOperations mongoOperations;

    /**
     * Constructeur de la classe SequenceGeneratorService.
     * Initialise la classe avec MongoOperations.
     *
     * @param mongoOperations les opérations MongoDB utilisées pour les séquences
     */
    @Autowired
    public SequenceGeneratorService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    /**
     * Génère une séquence pour un nom de séquence donné.
     * Incrémente la séquence et retourne la nouvelle valeur.
     *
     * @param seqName le nom de la séquence
     * @return la nouvelle valeur de la séquence
     */
    public String generateSequence(String seqName) {

        MongoDBMysqlSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("seq",1), options().returnNew(true).upsert(true),
                MongoDBMysqlSequence.class);
        return String.valueOf(!Objects.isNull(counter) ? counter.getSeq() : 1);

    }
}