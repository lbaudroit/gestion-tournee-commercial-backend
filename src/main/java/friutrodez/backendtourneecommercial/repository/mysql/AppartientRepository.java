package friutrodez.backendtourneecommercial.repository.mysql;

import friutrodez.backendtourneecommercial.model.Appartient;
import friutrodez.backendtourneecommercial.model.AppartientKey;
import friutrodez.backendtourneecommercial.model.Itineraire;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface de repository pour la gestion des entités Appartient.
 * Hérite de JpaRepository pour fournir des opérations CRUD sur les entités Appartient.
 * Utilise AppartientKey comme clé primaire composite.
 * Annotée avec @Repository pour indiquer qu'il s'agit d'un composant Spring.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
@Repository
public interface AppartientRepository extends JpaRepository<Appartient, AppartientKey> {

    List<Appartient> findAllByIdEmbedded_Itineraire_Id(long idEmbeddedItineraireId);

    void deleteAllByIdEmbedded_Itineraire_Id(long idEmbeddedItineraireId);

    void deleteAppartientByIdEmbedded_Itineraire_Utilisateur(Utilisateur idEmbeddedItineraireUtilisateur);

    void deleteAppartientByIdEmbedded_Itineraire_UtilisateurAndIdEmbedded_Itineraire(Utilisateur idEmbeddedItineraireUtilisateur, Itineraire idEmbeddedItineraire);
}