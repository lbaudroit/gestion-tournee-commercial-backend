package friutrodez.backendtourneecommercial.service;

import com.mongodb.client.result.UpdateResult;
import friutrodez.backendtourneecommercial.exception.DonneesInvalidesException;
import friutrodez.backendtourneecommercial.exception.DonneesManquantesException;
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
    public Client creerUnClient(Client clientInformations,String idUser) {

        clientInformations.setIdUtilisateur(idUser);
        if(clientInformations.getNomEntreprise() == null || clientInformations.getNomEntreprise().trim().isBlank()) {
            throw new DonneesManquantesException("Le client n'a pas de nom");

        }
        Adresse adresse = clientInformations.getAdresse();
        if(adresse == null) {
            throw new DonneesManquantesException("Le client n'a pas d'email");
        }
        if(!addressToolsService.validateAdresse(adresse.getLibelle(),adresse.getCodePostal(),adresse.getVille())) {
            throw new DonneesInvalidesException("L'adresse du client est invalide.");
        }

        Double[] coordinates = addressToolsService.geolocateAdresse(adresse.getLibelle(), adresse.getCodePostal(), adresse.getVille());
        Coordonnees coordonnees = new Coordonnees(coordinates[1],coordinates[0]);
        clientInformations.setCoordonnees(coordonnees);

        clientMongoTemplate.save(clientInformations);
        return clientInformations;
    }

    public void modifierUnClient(String idClient,Client modifications,String idUser) {
        Adresse adresse = modifications.getAdresse();
        if(adresse == null) {
            throw new DonneesManquantesException("Le client n'a pas d'email");
        }

        Client baseClient = clientMongoTemplate.getOneClient(idClient,idUser);

        if(!baseClient.getAdresse().equals(modifications.getAdresse())) {
            if(!addressToolsService.validateAdresse(adresse.getLibelle(),adresse.getCodePostal(),adresse.getVille())) {
                throw new DonneesInvalidesException("L'adresse du client est invalide.");
            }
        }

        baseClient.setAdresse(modifications.getAdresse());
        Adresse adresseBase = baseClient.getAdresse();
        Double[] coordinates = addressToolsService.geolocateAdresse(adresseBase.getLibelle(),
                adresseBase.getCodePostal(), adresseBase.getVille());
        Coordonnees coordonnees = new Coordonnees(coordinates[1],coordinates[0]);
        baseClient.setCoordonnees(coordonnees);

        baseClient.setNomEntreprise(modifications.getNomEntreprise());
        baseClient.setDescriptif(modifications.getDescriptif());
        baseClient.setContact(modifications.getContact());
        baseClient.setClientEffectif(modifications.isClientEffectif());

        clientMongoTemplate.save(baseClient);

    }
}
