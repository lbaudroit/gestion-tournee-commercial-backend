package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.exception.AdresseInvalideException;
import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.model.Adresse;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Coordonnees;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import org.springframework.stereotype.Service;

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

    /**
     * @param clientMongoTemplate Le template mongoDB pour les clients.
     * @param validatorService    Un service pour valider la ressource.
     */
    public ClientService(ClientMongoTemplate clientMongoTemplate, ValidatorService validatorService) {
        this.clientMongoTemplate = clientMongoTemplate;
        this.validatorService = validatorService;
    }

    /**
     * Méthode pour ajouter un client dans la BD.
     * Des vérifications sont faites au préalable.
     *
     * @param clientData Les données du client a créé.
     * @param idUser     L'id de l'utilisateur qui veut créé le client.
     * @return le client créé.
     */
    public Client CreateOneClient(Client clientData, String idUser) {
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

        Client savedClient = clientMongoTemplate.getOneClient(idClient, idUser);

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
}
