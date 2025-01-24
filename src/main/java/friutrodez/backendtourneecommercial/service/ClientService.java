package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.model.Adresse;
import friutrodez.backendtourneecommercial.model.Client;
import friutrodez.backendtourneecommercial.model.Coordonnees;
import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Classe métier qui gére les clients
 */
@Service
public class ClientService {

    private final ClientMongoTemplate clientMongoTemplate;

    private final AdresseToolsService addressToolsService = new AdresseToolsService();

    @Autowired
    public ClientService(ClientMongoTemplate clientMongoTemplate) {
        this.clientMongoTemplate = clientMongoTemplate;
    }
    /**
     * Méthode pour ajouter un client dans la BD.
     * Des vérifications sont faits au préalables
     * @return le client créé
     */
    public Client creerUnClient(Client clientInformations) {
        Utilisateur utilisateur = (Utilisateur)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        clientInformations.setIdUtilisateur(String.valueOf(utilisateur.getId()));
        Adresse adresse = clientInformations.getAdresse();
        if(!addressToolsService.validateAdresse(adresse.getLibelle(),adresse.getCodePostal(),adresse.getVille())) {
            throw new DonneesInvalidesException("L'adresse du client est invalide.");
        }

        Double[] coordinates = addressToolsService.geolocateAdresse(adresse.getLibelle(), adresse.getCodePostal(), adresse.getVille());
        Coordonnees coordonnees = new Coordonnees(coordinates[1],coordinates[0]);
        clientInformations.setCoordonnees(coordonnees);

        clientMongoTemplate.save(clientInformations);
        return clientInformations;
    }
}
