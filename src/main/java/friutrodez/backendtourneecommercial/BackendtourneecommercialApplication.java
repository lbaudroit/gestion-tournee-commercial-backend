package friutrodez.backendtourneecommercial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Classe principale de l'application Backendtourneecommercial.
 * Cette classe initialise et lance l'application Spring Boot.
 * Elle configure également les connexions à MongoDB et MySQL.
 *
 * @author
 * Benjamin NICOL
 * Enzo CLUZEL
 * Leïla BAUDROIT
 * Ahmed BRIBACH
 */
@SpringBootApplication
public class BackendtourneecommercialApplication {

    /**
     * Méthode principale pour lancer l'application Spring Boot.
     *
     * @param args les arguments de la ligne de commande
     */
    public static void main(String[] args) {
        SpringApplication.run(BackendtourneecommercialApplication.class, args);
    }

}