package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.dto.ResultatOptimisation;
import friutrodez.backendtourneecommercial.model.Client;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class ItineraireService {
    public ResultatOptimisation optimiserPlusCourt(List<Client> clients) {
        // STUB
        Collections.shuffle(clients);
        int kilometres = (int) (Math.random() * 1000);
        return new ResultatOptimisation(clients, kilometres);
    }
}
