
package Clients;

import Clients.Client;
import Lists.List;
import java.util.ArrayList;


public class ClientList implements List<Client> {
    
    ArrayList <Client> Array;
    private static ClientList instance;
    
    
    //instan
    public static ClientList getInstance() {
        if (instance == null) {
            instance = new ClientList();
        }
        return instance;
    }
    
    
    //metodo get
    public ArrayList<Client> getArray() {
        return Array;
    }

    //Contruc
    public ClientList() {
        this.Array = new ArrayList<>();
    }
    
    
    //metodos 
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
}
    
     

