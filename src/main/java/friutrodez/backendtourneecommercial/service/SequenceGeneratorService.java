package friutrodez.backendtourneecommercial.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import friutrodez.backendtourneecommercial.model.MongoDBMysqlSequence;

/**
 * Service pour gérer les ids en mongoDB.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
@Service
public class SequenceGeneratorService {

    private final MongoOperations mongoOperations;

    public SequenceGeneratorService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    /**
     * Méthode pour générer un nouvel id pour un document mongoDB.
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