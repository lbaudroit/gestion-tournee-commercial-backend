package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.model.MongoDBMysqlSequence;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Service pour gérer les ids en mongoDB.
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
@Service
public class SequenceGeneratorService {

    private final MongoOperations mongoOperations;

    public SequenceGeneratorService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    /**
     * Méthode pour générer un nouvel id pour un document mongoDB.
     *
     * @param seqName Le nom de la séquence.
     * @return L'id du document.
     */
    public String generateSequence(String seqName) {
        Query query = query(where("_id").is(seqName));
        Update update = new Update().inc("seq", 1);

        MongoDBMysqlSequence counter = mongoOperations.findAndModify(
                query,
                update,
                options().returnNew(true).upsert(true),
                MongoDBMysqlSequence.class);

        return String.valueOf(!Objects.isNull(counter) ? counter.getSeq() : 1);

    }
}