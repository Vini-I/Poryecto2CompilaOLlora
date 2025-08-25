/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reservations;

import Clients.Client;
import Clients.ClientList;
import Exceptions.*;
import Utils.UtilDate;
import Vehicles.*;
import java.time.LocalDateTime;

/**
 *
 * @author autoa
 */
public class Reservation {
    Client cliente;
    Vehicle car;
    VehicleType carType;
    LocalDateTime startTime;
    LocalDateTime finishTime;

    public Client getClient() {
        return cliente;
    }

    public Vehicle getCar() {
        return car;
    }

    public VehicleType getCarType() {
        return carType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public Reservation(Client cliente, Vehicle car, LocalDateTime finishTime) throws NoClientException, NoCarSelectedException, InvalidDateException {
        if (!validateClient(cliente.getId())) throw new NoClientException();
        this.cliente = cliente;
        if (!validateVehicle(car.getPlate())) throw new NoCarSelectedException();
        this.car = car;
        this.carType = car.getType();
        if (!validateDates(LocalDateTime.now(), finishTime)) throw new InvalidDateException();
        this.startTime = LocalDateTime.now();
        this.finishTime = finishTime;
    }

    public Reservation(Client cliente, Vehicle car, LocalDateTime startTime, LocalDateTime finishTime) {
        this.cliente = cliente;
        this.car = car;
        this.carType = car.getType();
        this.startTime = startTime;
        this.finishTime = finishTime;
    }
    
    public boolean validateClient(String id) {
        ClientList cL = ClientList.getInstance();
        return cL.find(id) != null;
    }
    
    public boolean validateVehicle(String plate) {
        VehicleList vL = VehicleList.getInstance();
        return vL.find(plate) != null;
    }
    
    public boolean modifyCar(Vehicle car) {
        if (car.getState() == VehicleState.AVAILABLE) {
            this.car = car;
            this.carType = car.getType();
            return true;
        }
        return false;
    }
    
    public boolean modifyDates(LocalDateTime startTime, LocalDateTime finishTime) {
        if (validateDates(startTime, finishTime)) {
            this.startTime = startTime;
            this.finishTime = finishTime;
            return true;
        }
        return false;
    }
    
    public boolean validateDates(LocalDateTime startTime, LocalDateTime finishTime) {
        if (UtilDate.isNotFutureDateTime(startTime, LocalDateTime.now())) return false;
        boolean startOk = UtilDate.isNotPastDateTime(startTime);
        boolean periodOk = UtilDate.periodIsValid(startTime, finishTime);
        return startOk && periodOk;
    }
    
    
}
