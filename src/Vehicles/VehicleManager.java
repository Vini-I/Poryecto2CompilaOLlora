/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vehicles;

import Exceptions.DuplicatedPlateException;
import Exceptions.RentedVehicleException;
import java.util.HashMap;

/**
 *
 * @author rodol
 * 
 */
public class VehicleManager {
    
    //Clase para manejar excepciones y validaciones
    private VehicleList vehicles = VehicleList.getInstance();

    public VehicleList getVehicles() {
        return vehicles;
    }
    
     public boolean addVehicle(Vehicle vehicle) throws DuplicatedPlateException {
         if (!vehicles.add(vehicle)){
             throw new DuplicatedPlateException();
         }
        return vehicles.add(vehicle);
    }

    public Vehicle findVehicle(String plate) {
        return vehicles.find(plate);
    }

    public boolean removeVehicle(Vehicle vehicle) throws RentedVehicleException {
        if (vehicle.getState() == VehicleState.RENTED) {
            throw new RentedVehicleException();
        }
        return vehicles.remove(vehicle);
    }
    
    public boolean updateVehicle(Vehicle updatedVehicle) {
        Vehicle vehicle = vehicles.find(updatedVehicle.getPlate());

        if (vehicle == null) {
            return false;
        }
        vehicle.setModel(updatedVehicle.getModel());
        vehicle.setType(updatedVehicle.getType());
        vehicle.setState(updatedVehicle.getState());
        return true;
    }
   
    public HashMap<String, Vehicle> getVehicleMap() {
        return vehicles.getMap();
    }
}
