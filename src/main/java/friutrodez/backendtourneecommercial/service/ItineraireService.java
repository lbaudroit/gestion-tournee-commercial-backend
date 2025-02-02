package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.dto.ClientId;
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
        return new ResultatOptimisation(transformToClientId(clients), kilometres);
    }

    private List<ClientId> transformToClientId(List<Client> clients) {
        List<ClientId> clientId = new ArrayList<>();
        System.out.println(clients);
        for (Client client : clients) {
            clientId.add(new ClientId(client.get_id()));
        }
        return clientId;
    }
}
