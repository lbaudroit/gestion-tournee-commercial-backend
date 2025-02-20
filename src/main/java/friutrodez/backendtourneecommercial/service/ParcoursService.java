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
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
@Service
public class ParcoursService {

    private final ParcoursMongoTemplate parcoursMongoTemplate;
    private final ValidatorService validatorService;

    private Parcours parcours;

    /**
     * construct ParcoursService
     * @param parcoursMongoTemplate
     * @param validatorService
     */
    public ParcoursService(ParcoursMongoTemplate parcoursMongoTemplate, ValidatorService validatorService) {
        this.parcoursMongoTemplate = parcoursMongoTemplate;
        this.validatorService = validatorService;

    }

    /**
     * crée un parcours en base de données
     * @param etapesParcoursList
     * @param nom
     * @param id
     * @return parcours
     */
    public Parcours createParcours(List<EtapesParcours> etapesParcoursList, String nom, String id) {
        Parcours parcours = Parcours.builder()
                .nom(nom)
                .etapes(etapesParcoursList)
                .idUtilisateur(id)
                .build();

        // Sauvegarde dans MongoDB
        parcoursMongoTemplate.save(parcours);

        return parcours;
    }

}