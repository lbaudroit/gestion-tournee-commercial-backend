package friutrodez.backendtourneecommercial.service;

import com.mongodb.client.result.DeleteResult;
import friutrodez.backendtourneecommercial.exception.AdresseInvalideException;
import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.model.Adresse;
import friutrodez.backendtourneecommercial.model.Client;
import java.util.List;
import friutrodez.backendtourneecommercial.model.Coordonnees;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import friutrodez.backendtourneecommercial.repository.mysql.AppartientRepository;
import friutrodez.backendtourneecommercial.repository.mysql.ItineraireRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Service de gestion des clients.
 * <p>
 * Cette classe fournit des méthodes pour ajouter ou modifier un client.
 * <p>
 * Elle contient toutes les vérifications métiers.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
@Service
public class ClientService {

    private final ClientMongoTemplate clientMongoTemplate;
    private final ValidatorService validatorService;

    private final AdresseToolsService addressToolsService = new AdresseToolsService();
    private final AppartientRepository appartientRepository;
    private final ItineraireRepository itineraireRepository;

    /**
     * @param clientMongoTemplate Le template mongoDB pour les clients.
     * @param validatorService    Un service pour valider la ressource.
     */
    public ClientService(ClientMongoTemplate clientMongoTemplate,
                         ValidatorService validatorService,
                         AppartientRepository appartientRepository,
                         ItineraireRepository itineraireRepository) {
        this.clientMongoTemplate = clientMongoTemplate;
        this.validatorService = validatorService;
        this.appartientRepository = appartientRepository;
        this.itineraireRepository = itineraireRepository;
    }

    /**
     * Méthode pour ajouter un client dans la BD.
     * Des vérifications sont faites au préalable.
     *
     * @param clientData Les données du client a créé.
     * @param idUser     L'id de l'utilisateur qui veut créé le client.
     * @return le client créé.
     */
    public Client createOneClient(Client clientData, String idUser) {
        validatorService.mustValidate(clientData);
        clientData.setIdUtilisateur(idUser);

        Adresse address = clientData.getAdresse();

        if (!addressToolsService.validateAdresse(address.getLibelle(), address.getCodePostal(), address.getVille())) {
            throw new AdresseInvalideException("L'adresse du client est invalide.");
        }

        Double[] coordinates = addressToolsService.geolocateAdresse(address.getLibelle(), address.getCodePostal(), address.getVille());
        Coordonnees coordinatesObject = new Coordonnees(coordinates[1], coordinates[0]);
        clientData.setCoordonnees(coordinatesObject);

        clientMongoTemplate.save(clientData);
        return clientData;
    }

    /**
     * Méthode pour modifier un client de la BD.
     *
     * @param idClient L'id du client a modifié.
     * @param editData Les modifications apportées au client.
     * @param idUser   L'id de l'utilisateur qui veut modifier le client.
     */
    public void editOneClient(String idClient, Client editData, String idUser) {
        validatorService.mustValidate(editData);
        Adresse address = editData.getAdresse();

        Optional<Client> savedClientOpt = clientMongoTemplate.getOneClient(idClient, idUser);

        if (savedClientOpt.isEmpty()) {
            throw new NoSuchElementException("Le client n'a pas été trouvé");
        }

        Client savedClient = savedClientOpt.get();

        if (!savedClient.getAdresse().equals(editData.getAdresse())) {
            if (!addressToolsService.validateAdresse(address.getLibelle(), address.getCodePostal(), address.getVille())) {
                throw new AdresseInvalideException("L'adresse du client est invalide.");
            }
        }

        savedClient.setAdresse(editData.getAdresse());
        Adresse savedAddress = savedClient.getAdresse();
        Double[] coordinates = addressToolsService.geolocateAdresse(savedAddress.getLibelle(),
                savedAddress.getCodePostal(), savedAddress.getVille());
        if (coordinates == null) {
            throw new DonneesInvalidesException("L'adresse du client sauvegardée est invalide");
        }
        Coordonnees coordinatesObject = new Coordonnees(coordinates[1], coordinates[0]);
        savedClient.setCoordonnees(coordinatesObject);

        savedClient.setNomEntreprise(editData.getNomEntreprise());
        savedClient.setDescriptif(editData.getDescriptif());
        savedClient.setContact(editData.getContact());
        savedClient.setClientEffectif(editData.isClientEffectif());

        clientMongoTemplate.save(savedClient);
    }

    @Transactional
    public void deleteOneClient(String idClient, Utilisateur user) {
        // On supprime le client : on peut le faire en premier (pas de FK, car dans MongoDB)
        DeleteResult deleteResult = clientMongoTemplate.removeClientsWithId(idClient, String.valueOf(user.getId()));
        if (!deleteResult.wasAcknowledged() || deleteResult.getDeletedCount() == 0) {
            throw new NoSuchElementException("Le client n'a pas été trouvé");
        }

        // Ses appartenances et ses itinéraires liés (règle métier)
        appartientRepository.findAllByIdEmbedded_ClientId(idClient)
                .stream()
                .map(appartient -> appartient.getIdEmbedded().getItineraire())
                .distinct()
                .forEach(i -> {
                    appartientRepository.deleteAppartientByIdEmbedded_Itineraire_UtilisateurAndIdEmbedded_Itineraire(
                            user, i);
                    itineraireRepository.deleteByIdAndUtilisateur(i.getId(), user);
                });
    }

    /**
     * Méthode pour récupérer tous les prospects se trouvant dans un rayon de 1000 mètres de la position.
     * @param point Le centre du rayon.
     * @return Les clients dans le cercle de 1000 mètres.
     */
    public List<Client> getAllProspectsAround(Coordonnees point, String idUser) {
        return clientMongoTemplate.getAllProspectsAround(point,idUser);
    }
}
