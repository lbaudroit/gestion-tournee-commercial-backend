package friutrodez.backendtourneecommercial.repository.mysql;

import friutrodez.backendtourneecommercial.model.Utilisateur;
import jdk.jshell.execution.Util;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface de repository pour la gestion des entités Utilisateur.
 * Hérite de JpaRepository pour fournir des opérations CRUD sur les entités Utilisateur.
 * Annotée avec @Repository pour indiquer qu'il s'agit d'un composant Spring.
 *
 * @author
 * Benjamin NICOL
 * Enzo CLUZEL
 * Leïla BAUDROIT
 * Ahmed BRIBACH
 */
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    public Utilisateur findByNom(String nom);
    public Utilisateur findByEmail(String email);
}
