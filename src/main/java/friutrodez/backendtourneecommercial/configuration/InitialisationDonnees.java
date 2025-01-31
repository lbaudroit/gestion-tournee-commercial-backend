package friutrodez.backendtourneecommercial.configuration;

import friutrodez.backendtourneecommercial.model.*;
import friutrodez.backendtourneecommercial.repository.mongodb.ClientMongoTemplate;
import friutrodez.backendtourneecommercial.repository.mongodb.FakeDataService;
import friutrodez.backendtourneecommercial.repository.mysql.AppartientRepository;
import friutrodez.backendtourneecommercial.repository.mysql.ItineraireRepository;
import friutrodez.backendtourneecommercial.repository.mysql.UtilisateurRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe de configuration pour l'initialisation des données.
 * Cette classe initialise les données des utilisateurs, itinéraires, clients et appartenances
 * dans les bases de données MySQL et MongoDB au démarrage de l'application.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
@Configuration
public class InitialisationDonnees {

    @Autowired
    Environment environment;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private ItineraireRepository itineraireRepository;

    @Autowired
    private AppartientRepository appartientRepository;

    @Autowired
    private ClientMongoTemplate clientMongoTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FakeDataService fakeDataService;


    /**
     * Méthode d'initialisation des données.
     * Cette méthode est exécutée au démarrage de l'application pour insérer les données
     * initiales dans les bases de données MySQL et MongoDB.
     *
     * @return CommandLineRunner
     */
    @Bean
    public CommandLineRunner init() {
        return args -> {

            // Initialisation des utilisateurs
            Utilisateur utilisateur1 = Utilisateur.builder()
                .nom("Cluzel")
                .prenom("Enzo")
                    .email("en@cl.fr")
                    .motDePasse(passwordEncoder.encode("Enzo_123"))
                .libelleAdresse("50 Avenue de Bordeaux")
                .codePostal("12000")
                .ville("Rodez")
                .latitude(44.3602355)
                .longitude(2.5732901)
                .build();
            Utilisateur utilisateur2 = Utilisateur.builder()
                .nom("Baudroït")
                .prenom("Leïla")
                    .email("le@ba.fr")
                .motDePasse(passwordEncoder.encode("Leila_123"))
                .libelleAdresse("2 Rue du Duc d'Arpajon")
                .codePostal("12150")
                .ville("Sévérac-d'Aveyron")
                .latitude(44.3223815)
                .longitude(3.0666963)
                .build();
            Utilisateur utilisateur3 = Utilisateur.builder()
                .nom("Bribach")
                .prenom("Ahmed")
                    .email("ah@br.fr")
                .motDePasse(passwordEncoder.encode("Ahmed_123"))
                .libelleAdresse("1 Av. de Brommat")
                .codePostal("12600")
                .ville("Mur-de-Barrez")
                .latitude(44.8485)
                .longitude(2.6583)
                .build();
            Utilisateur utilisateur4 = Utilisateur.builder()
                .nom("Nicol")
                .prenom("Benjamin")
                    .email("be@ni.fr")
                .motDePasse(passwordEncoder.encode("Benjamin.123"))
                .libelleAdresse("6 Impasse du Suc")
                .codePostal("81490")
                .ville("Boissezon")
                .latitude(43.5775202)
                .longitude(2.3694482)
                .build();
            utilisateurRepository.saveAll(List.of(utilisateur1, utilisateur2, utilisateur3, utilisateur4));

            // Initialisation des itinéraires
            Itineraire itineraire1 = Itineraire.builder()
                    .utilisateur(utilisateur1)
                    .nom("Les 7 collines de Rodez")
                    .distance(15)
                    .build();
            Itineraire itineraire2 = Itineraire.builder()
                    .utilisateur(utilisateur2)
                    .nom("La longue route")
                    .distance(30)
                    .build();
            itineraireRepository.saveAll(List.of(itineraire1, itineraire2));
            // Itinéraires supplémentaires
            List<Itineraire> itineraires = new ArrayList<>();
            String titreTemplate = "Itinéraire n°%d";
            for (int i = 1; i <= 49; i++) {
                Itineraire itineraire = Itineraire.builder()
                    .utilisateur(utilisateur1)
                    .nom(String.format(titreTemplate, i))
                    .distance(i * 10)
                    .build();
                itineraires.add(itineraire);
            }
            itineraireRepository.saveAll(itineraires);

            // Initialisation des clients
            List<Client> clients = List.of(
                Client.builder()
                    .idUtilisateur("1")
                    .nomEntreprise("Boulangerie du Vieux Pont")
                    .adresse(new Adresse("3 Pl. du Bourg", "12000", "Rodez"))
                    .descriptif("Boulangerie artisanale réputée pour ses pains au levain et ses viennoiseries faites maison.")
                    .coordonnees(new Coordonnees(44.3489985, 2.5731058))
                    .contact(new Contact("Morel", "Jacques", "0565321478"))
                    .clientEffectif(false)
                    .build(),
                Client.builder()
                    .idUtilisateur("1")
                    .nomEntreprise("Garage Rouergue Auto")
                    .adresse(new Adresse("11 Av. du Ségala", "12000", "Le Monastère"))
                    .descriptif("Spécialiste en réparation automobile et entretien de véhicules toutes marques.")
                    .coordonnees(new Coordonnees(44.3350156, 2.576037))
                    .contact(new Contact("Durand", "Alain", "0565452233"))
                    .clientEffectif(true)
                    .build(),
                Client.builder()
                    .idUtilisateur("1")
                    .nomEntreprise("Restaurant L'Aubrac Gourmand")
                    .adresse(new Adresse("15 Av. de Laguiole", "12500", "Espalion"))
                    .descriptif("Restaurant gastronomique mettant en avant les produits locaux, notamment l'aligot et la viande d'Aubrac.")
                    .coordonnees(new Coordonnees(44.5259912, 2.7635276))
                    .contact(new Contact("Bernard", "Nathalie", "0565412312"))
                    .clientEffectif(true)
                    .build(),
                Client.builder()
                    .idUtilisateur("2")
                    .nomEntreprise("La Ferme du Lévezou")
                    .adresse(new Adresse("83 La Mouline", "12000", "Olemps"))
                    .descriptif("Exploitation agricole produisant des fromages artisanaux et des charcuteries traditionnelles.")
                    .coordonnees(new Coordonnees(44.3300483, 2.5593095))
                    .contact(new Contact("Fournier", "Benoît", "0565341298"))
                    .clientEffectif(false)
                    .build(),
                Client.builder()
                    .idUtilisateur("2")
                    .nomEntreprise("Cave de Millau")
                    .adresse(new Adresse("12 Imp. des Vignes", "12100", "Millau"))
                    .descriptif("Caviste proposant une large sélection de vins locaux et spiritueux d'exception.")
                    .coordonnees(new Coordonnees(44.1089357, 3.0621842))
                    .contact(new Contact("Roussel", "Isabelle", "0565611834"))
                    .clientEffectif(true)
                    .build(),
                Client.builder()
                    .idUtilisateur("2")
                    .nomEntreprise("École de Parapente des Gorges")
                    .adresse(new Adresse("64 Avenue de Bordeaux", "12000", "Rodez"))
                    .descriptif("Formation et baptêmes de parapente dans un cadre exceptionnel des Gorges de l'Aveyron.")
                    .coordonnees(new Coordonnees(44.3607091, 2.575482))
                    .contact(new Contact("Pons", "Luc", "0565672145"))
                    .clientEffectif(true)
                    .build(),
                Client.builder()
                    .idUtilisateur("2")
                    .nomEntreprise("Pharmacie du Caussanel")
                    .adresse(new Adresse("7 Boulevard des Remparts", "12400", "Saint-Affrique"))
                    .descriptif("Pharmacie et parapharmacie avec un service de conseil personnalisé.")
                    .coordonnees(new Coordonnees(43.9596889, 2.8847171))
                    .contact(new Contact("Martin", "Sophie", "0565582367"))
                    .clientEffectif(true)
                    .build(),
                Client.builder()
                    .idUtilisateur("1")
                    .nomEntreprise("Hôtel des Tilleuls")
                    .adresse(new Adresse("2 Rue de Bourran", "12740", "Sébazac-Concourès"))
                    .descriptif("Petit hôtel familial offrant un séjour calme et chaleureux.")
                    .coordonnees(new Coordonnees(44.5392277, 2.5618809))
                    .contact(new Contact("Lacombe", "Paul", "0565731545"))
                    .clientEffectif(true)
                    .build(),
                Client.builder()
                    .idUtilisateur("3")
                    .nomEntreprise("Ébénisterie Aveyronnaise")
                    .adresse(new Adresse("7 Rue Foncourieu", "12330", "Marcillac-Vallon"))
                    .descriptif("Fabrication artisanale de meubles sur mesure en bois local.")
                    .coordonnees(new Coordonnees(44.4761361, 2.4414059))
                    .contact(new Contact("Garnier", "Michel", "0565632789"))
                    .clientEffectif(true)
                    .build(),
                Client.builder()
                    .idUtilisateur("1")
                    .nomEntreprise("Charcuterie Arnal")
                    .adresse(new Adresse("1 Rte de l'Aubrac", "12210", "Laguiole"))
                    .descriptif("Producteur de charcuterie et spécialités aveyronnaises, dont le saucisson de montagne.")
                    .coordonnees(new Coordonnees(44.6840223, 2.8513199))
                    .contact(new Contact("Arnal", "Anne", "0565441890"))
                    .clientEffectif(false)
                    .build(),
                Client.builder()
                    .idUtilisateur("2")
                    .nomEntreprise("Les Jardins de l’Aubrac")
                    .adresse(new Adresse("20 Av. de Rodez", "12290", "Pont-de-Salars"))
                    .descriptif("Pépinière et jardinier paysagiste spécialisé dans les plantes locales et l'aménagement extérieur.")
                    .coordonnees(new Coordonnees(44.2792943, 2.7271058))
                    .contact(new Contact("Dupuis", "Claire", "0565461234"))
                    .clientEffectif(true)
                    .build(),
                Client.builder()
                    .idUtilisateur("1")
                    .nomEntreprise("Maison d’Hôtes Les Cyprès")
                    .adresse(new Adresse("10 Rte du Ségala", "12270", "La Fouillade"))
                    .descriptif("Charmante maison d’hôtes au cœur du village médiéval, idéale pour les escapades romantiques.")
                    .coordonnees(new Coordonnees(44.2308501, 2.0415595))
                    .contact(new Contact("Barret", "Catherine", "0565293478"))
                    .clientEffectif(true)
                    .build(),
                Client.builder()
                    .idUtilisateur("1")
                    .nomEntreprise("Atelier de Poterie Rouergue")
                    .adresse(new Adresse("11 Rte d'Occitanie", "12270", "La Fouillade"))
                    .descriptif("Atelier artisanal proposant des poteries et céramiques décoratives.")
                    .coordonnees(new Coordonnees(44.2308501, 2.0415595))
                    .contact(new Contact("Mercier", "Thomas", "0565401122"))
                    .clientEffectif(true)
                    .build(),
                Client.builder()
                    .idUtilisateur("1")
                    .nomEntreprise("Fromagerie du Larzac")
                    .adresse(new Adresse("9 lotissement Le Seryeis", "12230", "La Cavalerie"))
                    .descriptif("Fromagerie traditionnelle produisant du Roquefort et d’autres spécialités locales.")
                    .coordonnees(new Coordonnees(44.0076749, 3.1608165))
                    .contact(new Contact("Lemoine", "Gérard", "0565312254"))
                    .clientEffectif(true)
                    .build(),
                Client.builder()
                    .idUtilisateur("1")
                    .nomEntreprise("Bijouterie Aubrac Or")
                    .adresse(new Adresse("24 Rue du Barry", "12150", "Sévérac-d'Aveyron"))
                    .descriptif("Bijouterie proposant des créations sur mesure en or et argent.")
                    .coordonnees(new Coordonnees(44.3215041, 3.0721795))
                    .contact(new Contact("Valette", "Sophie", "0565473912"))
                    .clientEffectif(false)
                    .build(),
                Client.builder()
                    .idUtilisateur("2")
                    .nomEntreprise("Écuries des Causses")
                    .adresse(new Adresse("7 Av. du Rouergue", "12240", "Rieupeyroux"))
                    .descriptif("Centre équestre offrant cours, stages et promenades à cheval dans les Causses.")
                    .coordonnees(new Coordonnees(44.3082695, 2.240297))
                    .contact(new Contact("Garnier", "Julien", "0565567890"))
                    .clientEffectif(true)
                    .build(),
                Client.builder()
                    .idUtilisateur("2")
                    .nomEntreprise("Librairie du Ségala")
                    .adresse(new Adresse("8 Rue du Balat", "12240", "Rieupeyroux"))
                    .descriptif("Librairie indépendante proposant une large sélection d’ouvrages et d’auteurs locaux.")
                    .coordonnees(new Coordonnees(44.3086837, 2.2354147))
                    .contact(new Contact("Perrier", "Louise", "0565741387"))
                    .clientEffectif(true)
                    .build(),
                Client.builder()
                    .idUtilisateur("2")
                    .nomEntreprise("Brasserie des Deux Vallées")
                    .adresse(new Adresse("95 Av. Maruejouls", "12300", "Decazeville"))
                    .descriptif("Microbrasserie produisant des bières artisanales aux saveurs uniques.")
                    .coordonnees(new Coordonnees(44.5640878, 2.2497527))
                    .contact(new Contact("Blanc", "Hugo", "0565712236"))
                    .clientEffectif(true)
                    .build(),
                Client.builder()
                    .idUtilisateur("2")
                    .nomEntreprise("Camping Les Gorges du Tarn")
                    .adresse(new Adresse("7 Pl. de la Capelle", "12100", "Millau"))
                    .descriptif("Camping en bordure du Tarn, idéal pour les amateurs de nature et d'activités en plein air.")
                    .coordonnees(new Coordonnees(44.1019, 3.0777094))
                    .contact(new Contact("Delmas", "Margaux", "0565416745"))
                    .clientEffectif(false)
                    .build(),
                Client.builder()
                    .idUtilisateur("1")
                    .nomEntreprise("Menuiserie Lavigne")
                    .adresse(new Adresse("11 Pl. du Portail Haut", "12390", "Rignac"))
                    .descriptif("Entreprise spécialisée dans les travaux de menuiserie et l’aménagement intérieur.")
                    .coordonnees(new Coordonnees(44.4084791, 2.2882397))
                    .contact(new Contact("Lavigne", "Pascal", "0565683490"))
                    .clientEffectif(true)
                    .build()
            );

            // In InitialisationDonnees.java, update the fake clients generation part:
            clientMongoTemplate.removeAll();
            // Generate 500 fake clients for Enzo (utilisateur1)
            //List<Client> fakeClients = fakeDataService.generateFakeClients(500);
            //clientMongoTemplate.saveAll(fakeClients);
            // Then save your real clients
            clientMongoTemplate.saveAll(clients);


            // Initialisation des appartenances
            Appartient appartient1 = new Appartient(new AppartientKey(itineraire1, clients.get(0).get_id()), 2);
            Appartient appartient2 = new Appartient(new AppartientKey(itineraire1, clients.get(1).get_id()), 1);
            Appartient appartient3 = new Appartient(new AppartientKey(itineraire1, clients.get(2).get_id()), 0);
            Appartient appartient4 = new Appartient(new AppartientKey(itineraire2, clients.get(3).get_id()), 0);
            Appartient appartient5 = new Appartient(new AppartientKey(itineraire2, clients.get(4).get_id()), 1);
            Appartient appartient6 = new Appartient(new AppartientKey(itineraire2, clients.get(5).get_id()), 2);
            Appartient appartient7 = new Appartient(new AppartientKey(itineraire2, clients.get(6).get_id()), 3);
            List<Appartient> appartients = new ArrayList<>();
            List<Client> clientsUtilisateur1 = new ArrayList<>();
            for (Client client : clients) {
                if (client.getIdUtilisateur().equals("1")) {
                    clientsUtilisateur1.add(client);
                }
            }
            System.out.println(clientsUtilisateur1.size());
            for (Itineraire itineraire : itineraires) {
                List<Client> tmp = new ArrayList<>(clientsUtilisateur1);
                int nbAleatoire = (int) (Math.random() * tmp.size());
                for (int i = 0; i < nbAleatoire; i++) {
                    int choisi = (int) (Math.random() * tmp.size());
                    Appartient appartient = new Appartient(new AppartientKey(itineraire, tmp.get(choisi).get_id()), i);
                    tmp.remove(choisi);
                    appartients.add(appartient);
                }
            }
            appartientRepository.saveAll(appartients);



            appartientRepository.saveAll(List.of(appartient1, appartient2, appartient3, appartient4, appartient5, appartient6, appartient7));
        };
    }
}