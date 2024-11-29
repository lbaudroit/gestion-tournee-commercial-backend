package friutrodez.backendtourneecommercial.controller;

import friutrodez.backendtourneecommercial.model.Utilisateur;
import friutrodez.backendtourneecommercial.repository.mysql.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path = "/")
public class HelloWorldController {


    @Autowired
    UtilisateurRepository utilisateurRepository;
    @GetMapping(path = "hello")
    public String hello() throws Exception {
        Optional<Utilisateur> utilisateurPresent = utilisateurRepository.findById(1L);

        if(utilisateurPresent.isPresent()) {
            throw new Exception("Present");
        }
        Utilisateur utilisateur= new Utilisateur();
        utilisateur.setId(1L);
        utilisateur.setNom("en");
        utilisateur.setPrenom("en");


        utilisateurRepository.save(utilisateur);

        return "Hello";
    }
}
