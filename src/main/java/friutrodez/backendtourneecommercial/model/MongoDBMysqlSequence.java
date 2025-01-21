package friutrodez.backendtourneecommercial.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Classe représentant une séquence de base de données MongoDB.
 * Cette classe est utilisée pour gérer les séquences auto-incrémentées dans MongoDB.
 * Utilise Lombok pour générer les constructeurs, getters et setters.
 *
 * @author
 * Benjamin NICOL
 * Enzo CLUZEL
 * Leïla BAUDROIT
 * Ahmed BRIBACH
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "database_sequences")
public class MongoDBMysqlSequence {

    /**
     * Identifiant unique de la séquence.
     */
    @Id
    private String id;

    /**
     * Valeur de la séquence.
     */
    private long seq;
}