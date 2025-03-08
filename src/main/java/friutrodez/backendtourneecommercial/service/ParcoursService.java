package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.dto.ParcoursDTO;
import friutrodez.backendtourneecommercial.dto.ParcoursReducedDTO;
import friutrodez.backendtourneecommercial.model.EtapesParcours;
import friutrodez.backendtourneecommercial.model.Parcours;
import friutrodez.backendtourneecommercial.repository.mongodb.ParcoursMongoTemplate;
import org.springframework.data.domain.Pageable;
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
     * @param dto DTO du parcours
     * @param id ID de l'utilisateur
     */
    public void createParcours(ParcoursDTO dto, String id) {
        Parcours parcours = Parcours.builder()
                .nom(dto.nom())
                .etapes(dto.etapes())
                .chemin(dto.chemin())
                .dateDebut(dto.debut())
                .dateFin(dto.fin())
                .idUtilisateur(id)
                .build();

        parcoursMongoTemplate.save(parcours);
    }

    /**
     * Récupère une liste paginée de parcours réduits pour un utilisateur donné.
     *
     * @param idUser L'ID de l'utilisateur
     * @param pageable Les informations de pagination
     * @return Une liste de ParcoursReducedDTO
     */
    public List<ParcoursReducedDTO> getLazyReducedParcours(String idUser, Pageable pageable) {
        List<Parcours> parcours = parcoursMongoTemplate.getParcoursByPage(idUser, pageable);
        return ParcoursReducedDTO.fromParcours(parcours);
    }
}