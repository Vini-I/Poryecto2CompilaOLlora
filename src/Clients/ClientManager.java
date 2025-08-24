
package Clients;

import Exceptions.ActiveReservationException;
import Exceptions.RegisterClientsException;


public class ClientManager {
    
ClientList clients = ClientList.getInstance();

    public boolean addClient(Client client) throws RegisterClientsException {
        if (client.getNumberLicense() == null){
            throw new RegisterClientsException();
        }
        return clients.addClient(client);
    }

    public Client findClient(String Id) {
        return clients.find(Id);
    }

    //public boolean removeClient(Client client) throws ActiveReservationException {
    //}

    public boolean updateClient(Client client) {
        Client cliens = clients.find(client.getId());
         if (cliens == null) {
        return false; 
        }
         
        client.setLicenseType(client.getLicenseType());
        client.setMail(client.getMail());
        client.setNumberLicense(client.getNumberLicense());
        client.setPhone(client.getPhone());
        
         return true;
    }
}