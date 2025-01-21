package friutrodez.backendtourneecommercial.repository.mysql;

import friutrodez.backendtourneecommercial.model.Appartient;
import friutrodez.backendtourneecommercial.model.AppartientKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface de repository pour la gestion des entités Appartient.
 * Hérite de JpaRepository pour fournir des opérations CRUD sur les entités Appartient.
 * Utilise AppartientKey comme clé primaire composite.
 * Annotée avec @Repository pour indiquer qu'il s'agit d'un composant Spring.
 *
 * @author
 * Benjamin NICOL
 * Enzo CLUZEL
 * Leïla BAUDROIT
 * Ahmed BRIBACH
 */
@Repository
public interface AppartientRepository extends JpaRepository<Appartient, AppartientKey> {

}