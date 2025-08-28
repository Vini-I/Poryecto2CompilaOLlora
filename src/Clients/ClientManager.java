
package Clients;

import Exceptions.ActiveReservationException;
import Exceptions.InvalidLicenseExcepcion;
import Exceptions.InvalidMailException;
import Exceptions.InvalidPhoneException;
import Exceptions.RegisterClientsException;
import Reservations.Reservation;
import Reservations.ReservationList;
import java.time.LocalDate;
import java.util.ArrayList;


public class ClientManager {

    //insta
private ClientList clients = ClientList.getInstance();
private ReservationList reservations = ReservationList.getInstance();

    //get
    public ClientList getClients() {
        return clients;
    }

    //metodos
    public boolean addClient(Client client) throws RegisterClientsException {
        if (client.getNumberLicense() == null){
            throw new RegisterClientsException();
        }
        return clients.add(client);
    }

    public Client findClient(String Id) {
        return clients.find(Id);
    }

    public boolean removeClient(Client client) throws ActiveReservationException {
        java.util.List<Reservation> clientReservations = reservations.searchReservations(client.getId());

        for (Reservation reservation : clientReservations) {
            if (LocalDate.now().isAfter(reservation.getStartTime().minusDays(1)) && LocalDate.now().isBefore(reservation.getFinishTime().plusDays(1))) {
                throw new ActiveReservationException();
            }
        }
        return clients.remove(client);
    }
    

    public boolean updateClient(Client updatedClient) throws InvalidMailException, InvalidPhoneException, InvalidLicenseExcepcion {
        Client client = clients.find(updatedClient.getId());
        
        if (client == null) {
        return false; 
        }
         
        client.setLicenseType(updatedClient.getLicenseType());
        client.setMail(updatedClient.getMail());
        client.setNumberLicense(updatedClient.getNumberLicense());
        client.setPhone(updatedClient.getPhone());
        
         return true;
    }

    //obtiene lista
    public ArrayList <Client> getArrayClient(){
        return clients.getArray();
    }
    }

   
    
