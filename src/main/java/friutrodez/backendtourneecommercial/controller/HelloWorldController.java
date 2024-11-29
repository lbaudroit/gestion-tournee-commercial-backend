package friutrodez.backendtourneecommercial.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
public class HelloWorldController {

    @GetMapping(path = "hello")
    public String hello() throws Exception {
        return "Hello";
    }
}
