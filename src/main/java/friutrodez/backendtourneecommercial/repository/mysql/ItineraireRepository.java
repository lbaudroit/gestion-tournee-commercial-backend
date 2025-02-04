package friutrodez.backendtourneecommercial.repository.mysql;

import friutrodez.backendtourneecommercial.model.Itineraire;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface de repository pour la gestion des entités Itineraire.
 * Hérite de JpaRepository pour fournir des opérations CRUD sur les entités Itineraire.
 * Annotée avec @Repository pour indiquer qu'il s'agit d'un composant Spring.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
@Repository
public interface ItineraireRepository extends JpaRepository<Itineraire, Long> {

    List<Itineraire> getItinerairesByUtilisateur(Utilisateur utilisateur);

    List<Itineraire> getItinerairesByUtilisateur(Utilisateur utilisateur, Pageable pageable);

    Optional<Itineraire> findItineraireByIdAndUtilisateur(long id, Utilisateur utilisateur);

    long countItineraireByUtilisateur(Utilisateur utilisateur);

    void deleteByIdAndUtilisateur(long id, Utilisateur utilisateur);
}