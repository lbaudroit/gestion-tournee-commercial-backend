package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.dto.ClientId;
import friutrodez.backendtourneecommercial.dto.ItineraireCreationDTO;
import friutrodez.backendtourneecommercial.dto.ResultatOptimisation;
import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.model.*;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import friutrodez.backendtourneecommercial.repository.mysql.AppartientRepository;
import friutrodez.backendtourneecommercial.repository.mysql.ItineraireRepository;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.Generator;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.BestRoute;
import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;
import jakarta.transaction.Transactional;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Service de gestion des itinéraires.
 * <p>
 * Cette classe fournit des méthodes pour ajouter, modifier un itineraire ou supprimer un itineraire.
 * <p>
 * Elle contient toutes les vérifications métiers d'un itineraire.
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
@Service
public class ItineraireService {

    private final ValidatorService validatorService;

    private final ClientMongoTemplate clientMongoTemplate;
    private final ItineraireRepository itineraireRepository;
    private final AppartientRepository appartientRepository;

    /**
     * @param validatorService     Un service pour valider la ressource
     * @param itineraireRepository Un repository pour les itineraires
     * @param appartientRepository Un repository pour manipuler les liaisons entre client et itineraire
     * @param clientMongoTemplate  Template des clients
     */
    public ItineraireService(ValidatorService validatorService, ClientMongoTemplate clientMongoTemplate, ItineraireRepository itineraireRepository, AppartientRepository appartientRepository) {
        this.validatorService = validatorService;
        this.clientMongoTemplate = clientMongoTemplate;
        this.itineraireRepository = itineraireRepository;
        this.appartientRepository = appartientRepository;
    }

    private static List<Point> getPointsFromClients(List<Client> clients) {
        List<Point> points = new ArrayList<>();
        for (Client client : clients) {
            String id = client.get_id();
            Coordonnees coordonnees = client.getCoordonnees();
            points.add(new Point(id, coordonnees.longitude(), coordonnees.latitude()));
        }
        return points;
    }

    /**
     * Optimise le trajet le plus court partant du domicile de l'utilisateur, visitant
     * l'adresse de chacun des clients et revenant au domicile.
     *
     * @param clients la liste de clients à visiter
     * @param user    l'utilisateur qui souhaite effectuer un itinéraire
     * @return une liste ordonnée de clients de manière à réduire le nombre de kilomètres
     * entre eux et le nombre de kilomètres associés à cet itinéraire
     */
    public ResultatOptimisation optimizeShortest(List<Client> clients, Utilisateur user) {
        List<Point> points = getPointsFromClients(clients);

        Generator generator = new Generator();
        BestRoute bestRoute = generator.run(points, user.getLongitude(), user.getLatitude(), Generator.DEFAULT_ALGORITHM);

        int kilometres = bestRoute.distance();
        List<Point> pointsOptimized = bestRoute.points();
        return new ResultatOptimisation(transformToClientId(pointsOptimized), kilometres);
    }

    /**
     * Récupère les identifiants des clients dans une liste de points.
     *
     * @param points les points (avec le même ID que les clients) dont on veut les identifiants
     * @return une liste des identifiants
     */
    private List<ClientId> transformToClientId(List<Point> points) {
        List<ClientId> clientIds = new ArrayList<>();
        for (Point point : points) {
            clientIds.add(new ClientId(point.getId()));
        }
        return clientIds;
    }

    /**
     * Méthode pour créer un itineraire dans la BD.
     *
     * @param itineraireData Les données de création de l'itineraire.
     * @param user           L'utilisateur qui veut créer le client
     * @return L'itineraire sauvegardé
     */
    @Transactional
    public Itineraire createItineraire(ItineraireCreationDTO itineraireData, Utilisateur user) {
        Itineraire aSauvegarder = Itineraire.builder()
                .nom(itineraireData.nom())
                .utilisateur(user)
                .distance(itineraireData.distance())
                .build();

        check(aSauvegarder, user, itineraireData);

        Itineraire itineraire = itineraireRepository.save(aSauvegarder);

        saveAppartientsFromListIdClients(itineraire, itineraireData.idClients());
        return itineraire;
    }

    /**
     * Méthode pour modifier un itineraire dans la BD.
     *
     * @param itineraireData Les données de modifications.
     * @param user           L'utilisateur qui veut modifier l'itineraire.
     * @param id             L'id de l'itineraire à modifier.
     * @return L'itineraire modifié
     */
    @Transactional
    public Itineraire editItineraire(ItineraireCreationDTO itineraireData, Utilisateur user, long id) {
        Itineraire aSauvegarder = Itineraire.builder()
                .id(id)
                .nom(itineraireData.nom())
                .utilisateur(user)
                .distance(itineraireData.distance())
                .build();

        check(aSauvegarder, user, itineraireData);
        Itineraire itineraire = itineraireRepository.save(aSauvegarder);
        appartientRepository.deleteAllByIdEmbedded_Itineraire_Id(id);
        saveAppartientsFromListIdClients(itineraire, itineraireData.idClients());
        return itineraire;
    }

    /**
     * Méthode pour supprimer un itineraire de la BD.
     *
     * @param itineraireId L'id de l'itineraire à supprimer.
     * @param user         L'utilisateur qui veut supprimer l'itineraire.
     */
    @Transactional
    public void deleteItineraire(long itineraireId, Utilisateur user) {
        Optional<Itineraire> itineraire = itineraireRepository.findById(itineraireId);
        if (itineraire.isPresent()) {
            appartientRepository.deleteAppartientByIdEmbedded_Itineraire_UtilisateurAndIdEmbedded_Itineraire(
                    user, itineraire.get());
            itineraireRepository.deleteById(itineraireId);
        } else {
            throw new DonneesInvalidesException("L'itinéraire n'existe pas.");
        }
    }

    /**
     * Méthode pour vérifier les données d'un itinéraire.
     *
     * @param itineraire L'itinéraire à vérifier.
     * @param user       L'utilisateur lié à l'itinéraire.
     * @param dto        Les informations de l'itinéraire.
     */
    public void check(Itineraire itineraire, Utilisateur user, ItineraireCreationDTO dto) {
        checkItineraire(itineraire);
        if (dto.idClients().length > Itineraire.MAX_CLIENTS) {
            throw new DonneesInvalidesException("Le nombre de clients ne doit pas être supérieur à " + Itineraire.MAX_CLIENTS + ".");
        }

        Query query = new Query(Criteria.where("_id").in(Arrays.stream(dto.idClients()).toList()));
        List<Client> list = clientMongoTemplate.mongoTemplate.
                find(query, Client.class);
        if (list.size() != dto.idClients().length) {
            throw new DonneesInvalidesException("Un id client est invalide.");
        }
        boolean oneIsNotFromCurrentUser = list.
                stream().
                anyMatch(client -> !client.getIdUtilisateur().equals(String.valueOf(user.getId())));
        if (oneIsNotFromCurrentUser) {
            throw new DonneesInvalidesException("Un id inséré n'appartient pas à l'utilisateur");
        }
    }

    /**
     * Persiste des liaisons entre itinéraire et cleints
     *
     * @param itineraire l'itinéraire à lier
     * @param ids        un tableau ordonné des identifiants des clients
     */
    private void saveAppartientsFromListIdClients(Itineraire itineraire, String[] ids) {
        List<Appartient> appartients = new ArrayList<>(ids.length);

        for (int position = 0; position < ids.length; position++) {
            String idClient = ids[position];
            appartients.add(new Appartient(new AppartientKey(itineraire, idClient), position));
        }

        appartientRepository.saveAll(appartients);
    }

    /**
     * Méthode pour vérifier un itineraire.
     *
     * @param itineraire L'itineraire à vérifier.
     */
    public void checkItineraire(Itineraire itineraire) {
        validatorService.mustValidate(itineraire);

        if (isDistancePositive(itineraire.getDistance())) {
            throw new DonneesInvalidesException("La distance doit être positive");
        }
    }

    /**
     * Méthode pour vérifier si la distance est positive
     *
     * @param distance La distance à vérifier
     * @return true si la distance est positive sinon false
     */
    private boolean isDistancePositive(int distance) {
        return distance < 0;
    }
}
