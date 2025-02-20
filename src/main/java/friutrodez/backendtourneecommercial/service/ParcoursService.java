package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.model.EtapesParcours;
import friutrodez.backendtourneecommercial.model.Parcours;
import friutrodez.backendtourneecommercial.repository.mongodb.ParcoursMongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service de gestion des parcours.
 * <p>
 * Cette classe fournit des méthodes pour ajouter un parcours.
 * <p>
 *
 * @author Benjamin NICOL
 * @version 1.0
 */
@Service
public class ParcoursService {

    private final ParcoursMongoTemplate parcoursMongoTemplate;

    /**
     * Constructeur de la classe ParcoursService.
     *
     * @param parcoursMongoTemplate Template MongoDB pour les parcours
     */
    public ParcoursService(ParcoursMongoTemplate parcoursMongoTemplate) {
        this.parcoursMongoTemplate = parcoursMongoTemplate;
    }

    /**
     * Crée un parcours en base de données.
     *
     * @param etapesParcoursList Liste des étapes du parcours
     * @param nom Nom du parcours
     * @param id ID de l'utilisateur
     */
    public void createParcours(List<EtapesParcours> etapesParcoursList, String nom, String id) {
        Parcours parcours = Parcours.builder()
                .nom(nom)
                .etapes(etapesParcoursList)
                .idUtilisateur(id)
                .build();

        parcoursMongoTemplate.save(parcours);
    }
}