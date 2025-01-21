package friutrodez.backendtourneecommercial.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour gérer les requêtes HTTP de base.
 * Ce contrôleur fournit un point de terminaison pour renvoyer un message de salutation.
 *
 * @author
 * Benjamin NICOL
 * Enzo CLUZEL
 * Leïla BAUDROIT
 * Ahmed BRIBACH
 */
@RestController
@RequestMapping(path = "/")
public class HelloWorldController {

    /**
     * Point de terminaison HTTP GET pour renvoyer un message de salutation.
     *
     * @return Une chaîne de caractères contenant "Hello".
     */
    @GetMapping(path = "hello")
    public String hello() {
        return "Hello";
    }
}