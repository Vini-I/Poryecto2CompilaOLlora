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
import java.time.LocalDate;

/**
 *
 * @author autoa
 */
public class Reservation {
    Client cliente;
    Vehicle car;
    VehicleType carType;
    LocalDate startTime;
    LocalDate finishTime;

    public Client getClient() {
        return cliente;
    }

    public Vehicle getCar() {
        return car;
    }

    public VehicleType getCarType() {
        return carType;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public LocalDate getFinishTime() {
        return finishTime;
    }

    public Reservation(Client cliente, Vehicle car, LocalDate finishTime) throws NoClientException, NoCarSelectedException, InvalidDateException {
        if (!validateClient(cliente.getId())) throw new NoClientException();
        this.cliente = cliente;
        if (!validateVehicle(car.getPlate())) throw new NoCarSelectedException();
        this.car = car;
        this.carType = car.getType();
        if (!validateDates(LocalDate.now(), finishTime)) throw new InvalidDateException();
        this.startTime = LocalDate.now();
        this.finishTime = finishTime;
    }

    public Reservation(Client cliente, Vehicle car, LocalDate startTime, LocalDate finishTime) throws NoClientException, NoCarSelectedException, InvalidDateException {
        if (!validateClient(cliente.getId())) throw new NoClientException();
        this.cliente = cliente;
        if (!validateVehicle(car.getPlate())) throw new NoCarSelectedException();
        this.car = car;
        this.carType = car.getType();
        if (!validateDates(startTime, finishTime)) throw new InvalidDateException();
        this.startTime = startTime;
        this.finishTime = finishTime;
    }
    
    public static boolean validateClient(String id) {
        ClientList cL = ClientList.getInstance();
        return cL.find(id) != null;
    }
    
    public static boolean validateVehicle(String plate) {
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
    
    public void modifyDates(LocalDate startTime, LocalDate finishTime) throws InvalidDateException {
        if (!validateDates(startTime, finishTime)) throw new InvalidDateException();
        this.startTime = startTime;
        this.finishTime = finishTime;
    }
    
    public static boolean validateDates(LocalDate startTime, LocalDate finishTime) {
        if (UtilDate.isNotFutureDate(startTime)) return false;
        boolean startOk = UtilDate.isNotPastDate(startTime);
        boolean periodOk = UtilDate.periodIsValid(startTime, finishTime);
        return startOk && periodOk;
    }
    
    
}
