
package Clients;
import Exceptions.ActiveReservationException;
import Exceptions.InvalidMailException;
import Exceptions.InvalidPhoneException;
import Exceptions.RegisterClientsException;
import java.util.ArrayList;


public class ClientManager {
    
private ClientList clients = ClientList.getInstance();

    public ClientList getClients() {
        return clients;
    }

    public boolean addClient(Client client) throws RegisterClientsException {
        if (client.getNumberLicense() == null){
            throw new RegisterClientsException();
        }
        return clients.add(client);
    }

    public Client findClient(String Id) {
        return clients.find(Id);
    }

    //public boolean removeClient(Client client) throws ActiveReservationException {
   //     if (client == ) {
    //        throw new ActiveReservationException();
      //  }
      //  return client.remove(client);
    //}
    

    public boolean updateClient(Client client) throws InvalidMailException, InvalidPhoneException {
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