package friutrodez.backendtourneecommercial.repository.mysql;

import friutrodez.backendtourneecommercial.model.Itineraire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface de repository pour la gestion des entités Itineraire.
 * Hérite de JpaRepository pour fournir des opérations CRUD sur les entités Itineraire.
 * Annotée avec @Repository pour indiquer qu'il s'agit d'un composant Spring.
 *
 * @author
 * Benjamin NICOL
 * Enzo CLUZEL
 * Leïla BAUDROIT
 * Ahmed BRIBACH
 */
@Repository
public interface ItineraireRepository extends JpaRepository<Itineraire, Long> {

}