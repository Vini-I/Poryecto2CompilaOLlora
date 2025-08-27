/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Contracts;

import Utils.UtilDate;
import Clients.Client;
import Exceptions.InvalidDateException;
import Exceptions.NoCarSelectedException;
import Exceptions.NoClientException;
import Exceptions.OverlappingReservationException;
import Reservations.*;
import Vehicles.Vehicle;
import Vehicles.VehicleState;
import java.time.LocalDateTime;

/**
 *
 * @author autoa
 */
public class Contract {
    private ReservationList list;
    private int contractNum;
    private Client client;
    private Vehicle vehicle;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private ContractState state;
    private TariffType tariff;
    private double totalAmount;

    public Contract(int contractNum, Client client, Vehicle vehicle, LocalDateTime startTime, LocalDateTime finishTime, TariffType tariff) 
            throws NoClientException, NoCarSelectedException, InvalidDateException, OverlappingReservationException {
        this.list = ReservationList.getInstance();
        this.contractNum = contractNum;
        this.client = client;
        this.vehicle = vehicle;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.state = ContractState.ACTIVE;
        this.tariff = tariff;
        this.totalAmount = calculateTotalAmount();
        this.vehicle.setState(VehicleState.RENTED);
        list.createReservation(client, vehicle, startTime, finishTime);
    }
    
    public Contract(int contractNum, Client client, Vehicle vehicle, LocalDateTime finishTime, TariffType tariff) 
            throws NoClientException, NoCarSelectedException, InvalidDateException, OverlappingReservationException {
        this.list = ReservationList.getInstance();
        this.contractNum = contractNum;
        this.client = client;
        this.vehicle = vehicle;
        this.startTime = LocalDateTime.now();
        this.finishTime = finishTime;
        this.state = ContractState.ACTIVE;
        this.tariff = tariff;
        this.totalAmount = calculateTotalAmount();
        this.vehicle.setState(VehicleState.RENTED);
        list.createReservation(client, vehicle, null, finishTime);
    }

    public Contract(int contractNum, Reservation reserva, TariffType tariff) {
        this.list = ReservationList.getInstance();
        this.contractNum = contractNum;
        this.client = reserva.getClient();
        this.vehicle = reserva.getCar();
        this.startTime = reserva.getStartTime();
        this.finishTime = reserva.getFinishTime();
        this.state = ContractState.ACTIVE;
        this.tariff = tariff;
        this.totalAmount = calculateTotalAmount();
        this.vehicle.setState(VehicleState.RENTED);
    }
    
    public int calculateRentalDuration() {
        return UtilDate.periodDuration(this.startTime, this.finishTime);
    }
    
    public double calculateTotalAmount() {
        int duration = calculateRentalDuration();
        return this.tariff.getTarifa() * duration;
    }
    
    public void finalizeContract() {
        this.state = ContractState.FINALIZED;
        this.vehicle.setState(VehicleState.AVAILABLE);
    }
    
    public void cancelContract() {
        if (this.state == ContractState.FINALIZED) return;
        this.state = ContractState.CANCELED;
        this.vehicle.setState(VehicleState.AVAILABLE);
    }

    public Client getClient() {
        return client;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public ContractState getState() {
        return state;
    }

    public TariffType getTariff() {
        return tariff;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
    
}
