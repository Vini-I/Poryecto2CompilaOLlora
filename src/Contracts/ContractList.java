/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Contracts;

import Clients.Client;
import Clients.ClientList;
import Exceptions.*;
import Utils.UtilDate;
import Vehicles.Vehicle;
import Vehicles.VehicleList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author autoa
 */
public class ContractList {
    private static int contractCounter = 0;
    private static ContractList instance;
    private HashMap<Client, HashSet<Contract>> listByClient;
    private HashMap<Vehicle, HashSet<Contract>> listByVehicle;
    
    public static ContractList getInstance() {
        if (instance == null) {
            instance = new ContractList();
        }
        return instance;
    }
    
    public String augmentContractNum() {
        contractCounter++;
        return String.valueOf(contractCounter);
    }

    private ContractList() {
        this.listByClient = new HashMap<>();
        this.listByVehicle = new HashMap<>();
    }
    
    public void addContract(Contract contrato) 
            throws ContractAlreadyExistsException, NoClientException, 
            NoCarSelectedException, InvalidDateException, OverlappingReservationException {
        validateContract(contrato);
        
        Client cliente = contrato.getClient();
        Vehicle vehiculo = contrato.getVehicle();
        
        listByClient.putIfAbsent(cliente, new HashSet<Contract>());
        listByVehicle.putIfAbsent(vehiculo, new HashSet<Contract>());
        
        boolean clientAddSuccess = listByClient.get(cliente).add(contrato);
        
        boolean vehicleAddSuccess = listByVehicle.get(vehiculo).add(contrato);
        
        if (!clientAddSuccess || !vehicleAddSuccess) {
            if (clientAddSuccess) {
                listByClient.get(cliente).remove(contrato);
            }
            if (vehicleAddSuccess) {
                listByVehicle.get(vehiculo).remove(contrato);
            }
            throw new ContractAlreadyExistsException();
        }
    }
    
    public List<Contract> getContractsByClientId(String cedula) {
        Client cliente = ClientList.getInstance().find(cedula);
        if (cliente != null) {
            return new ArrayList<>(listByClient.getOrDefault(cliente, new HashSet<>()));
        }
        return new ArrayList<>();
    }
    
    public List<Contract> getContractsByVehiclePlate(String vehiclePlate) {
        Vehicle vehicle = VehicleList.getInstance().find(vehiclePlate);
        if (vehicle != null) {
            return new ArrayList<>(listByVehicle.getOrDefault(vehicle, new HashSet<>()));
        }
        return new ArrayList<>();
    }
    
    public List<Contract> getContractByNumber(String contractNumber) {
        List<Contract> result = new ArrayList<>();
        for (HashSet<Contract> contracts : listByClient.values()) {
            for (Contract contract : contracts) {
                if (contract.getContractNum().equals(contractNumber)) {
                    result.add(contract);
                    return result;
                }
            }
        }
        return result;
    }
    
    public List<Contract> getAllContracts() {
        List<Contract> allContracts = new ArrayList<>();
        for (HashSet<Contract> contracts : listByClient.values()) {
            allContracts.addAll(contracts);
        }
        return allContracts;
    }
    
    private void validateContract(Contract contrato) throws NoClientException, NoCarSelectedException, InvalidDateException, OverlappingReservationException {
        validateClientAndVehicleExistence(contrato);
        validateContractDates(contrato);
        validateNoOverlap(contrato);
    }
    
    private void validateClientAndVehicleExistence(Contract contrato) throws NoClientException, NoCarSelectedException {
        Client client = ClientList.getInstance().find(contrato.getClient().getId());
        if (client == null) {
            throw new NoClientException();
        }

        Vehicle vehicle = VehicleList.getInstance().find(contrato.getVehicle().getPlate());
        if (vehicle == null) {
            throw new NoCarSelectedException();
        }
    }
    
    private void validateContractDates(Contract contrato) throws InvalidDateException {
        if (!UtilDate.isNotPastDate(contrato.getStartTime())) {
            throw new InvalidDateException();
        }

        if (contrato.getFinishTime().isBefore(contrato.getStartTime()) || contrato.getFinishTime().isEqual(contrato.getStartTime())) {
            throw new InvalidDateException();
        }
    }
    
    private void validateNoOverlap(Contract contrato) throws OverlappingReservationException {
        List<Contract> existingContracts = getContractsByVehiclePlate(contrato.getVehicle().getPlate());
        for (Contract existing : existingContracts) {
            if (existing == contrato) {
                continue;
            }
            
            if (UtilDate.isOverlapping(contrato.getStartTime(), contrato.getFinishTime(), existing.getStartTime(), existing.getFinishTime())) {
                throw new OverlappingReservationException();
            }
        }
    }

    public String getNextContractNumber() {
        contractCounter++;
        return String.valueOf(contractCounter);
    }
    
}
