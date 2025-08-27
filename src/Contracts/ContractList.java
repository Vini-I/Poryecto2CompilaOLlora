/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Contracts;

import Clients.Client;
import Exceptions.ContractAlreadyExistsException;
import Vehicles.Vehicle;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author autoa
 */
public class ContractList {
    private static ContractList instance;
    private HashMap<Client, HashSet<Contract>> listByClient;
    private HashMap<Vehicle, HashSet<Contract>> listByVehicle;
    
    public void addContract(Contract contrato) throws ContractAlreadyExistsException {
        Client cliente = contrato.getClient();
        Vehicle vehiculo = contrato.getVehicle();
        
        listByClient.putIfAbsent(cliente, new HashSet<Contract>());
        listByVehicle.putIfAbsent(vehiculo, new HashSet<Contract>());
        
        HashSet<Contract> hashCliente = listByClient.get(cliente);
        HashSet<Contract> hashCarro = listByVehicle.get(vehiculo);
        
        if (!hashCliente.add(contrato)) throw new ContractAlreadyExistsException();
        
        if (!hashCarro.add(contrato)) throw new ContractAlreadyExistsException();
    }
    
}
