
package Clients;

import Clients.Client;
import Lists.List;
import java.util.ArrayList;


public class ClientList implements List<Client> {
    
    ArrayList <Client> Array;
    private static ClientList instance;

    public ClientList() {
        this.Array = new ArrayList<>();
    }

    public ArrayList<Client> getArray() {
        return Array;
    }
    
    public static ClientList getInstance(){
        if(instance == null){
            instance = new ClientList();
        }
        return instance;
    }
    
    @Override
    public boolean add(Client t) {
       for (Client a : Array) {
            if (a.getId().equals(t.getId())) {
                return false;
            }
        }
        Array.add(t);
        return true;
    }

    @Override
    public Client find(Object id) {
        String strId = String.valueOf(id);
        for (Client a : Array) {
            if (a.getId().equals(strId)) {
                return a; 
            }
        }
        return null;
    }

    @Override
    public boolean remove(Client t) {
        for (Client a : Array) {
            if (a.getId().equals(t.getId())) {
                Array.remove(a);
                return true;
            }
        }
        return false; 
    }

    boolean addClient(Client client) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
     
}
