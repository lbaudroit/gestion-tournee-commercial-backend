package friutrodez.backendtourneecommercial.service;

import friutrodez.backendtourneecommercial.model.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Classe de test pour FakeDataServiceTest.
 *
 * @author Benjamin NICOL
 * @author Enzo CLUZEL
 * @author Leïla BAUDROIT
 * @author Ahmed BRIBACH
 */
class FakeDataServiceTest {

    /**
     * Teste la génération de faux clients.
     */
    @Test
    void testgenerateFakeClient() {
        FakeDataService fakeDataService = new FakeDataService();
        Client client = fakeDataService.generateFakeClient();
        Client client2 = fakeDataService.generateFakeClient();

        Assertions.assertNotNull(client);
        Assertions.assertNotNull(client2);

        Assertions.assertNotEquals(client,client2);
    }

    /**
     * Teste la génération de plusieurs faux clients.
     */
    @Test
    void testGenerateFakeClients() {
        FakeDataService fakeDataService = new FakeDataService();
        List<Client> clientList = fakeDataService.generateFakeClients(5);

        Assertions.assertEquals(5,clientList.size());
        Assertions.assertTrue(allNotEquals(clientList));
    }

    /**
     * Méthode pour vérifier que tous les clients ne sont pas égaux.
     * @param clientList La liste de clients à vérifier.
     * @return           True si tous les clients ne sont pas égaux sinon false.
     */
    private boolean allNotEquals(List<Client> clientList) {
        for(int i = 0 ; i< clientList.size() ; i++) {
            for(int j = 0 ; j < clientList.size() ; j++) {
                if(i!= j && clientList.get(i).equals(clientList.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

}