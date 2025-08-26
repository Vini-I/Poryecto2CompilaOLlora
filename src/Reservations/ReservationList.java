/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reservations;

import Clients.Client;
import Exceptions.*;
import Lists.List;
import Utils.UtilDate;
import Vehicles.Vehicle;
import Vehicles.VehicleList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Brwni
 */
public class ReservationList implements List<Vehicle> {
    
    HashMap<String, Queue<Reservation>> map;
    private static ReservationList instance;

    public static ReservationList getInstance() {
        if(instance == null) instance = new ReservationList();
        return instance;
    }
    
    private ReservationList() {
        this.map = new HashMap();
    }

    @Override
    public boolean add(Vehicle t) {
        return map.putIfAbsent(t.getPlate(), new LinkedList<>()) == null;
    }

    @Override
    public Vehicle find(Object id) {
        return null;
    }

    @Override
    public boolean remove(Vehicle t) {
        return false;
    }

    public boolean cancelReserve(String placa, Reservation reserva) {
        if (!UtilDate.periodIsValid(reserva.getStartTime(), LocalDateTime.now())) return false;
        
        Queue<Reservation> q = map.get(placa);
        if (q == null || q.isEmpty()) {
            return false;
        }

        return q.remove(reserva); 
    }
    
    public java.util.List<Reservation> searchReservations(String clienteId, LocalDate startDate, LocalDate endDate) {
        java.util.List<Reservation> result = new ArrayList<>();

        for (Queue<Reservation> cola : map.values()) {
            for (Reservation reserva : cola) {
                boolean match = true;

                // Filtro por cliente
                if (clienteId != null && !reserva.getClient().getId().equals(clienteId)) {
                    match = false;
                }

                // Filtro por rango de fechas
                if (startDate != null && endDate != null) {
                    if (reserva.getStartTime().toLocalDate().isAfter(endDate) ||
                        reserva.getFinishTime().toLocalDate().isBefore(startDate)) {
                        match = false;
                    }
                } else if (startDate != null) {
                    // Buscar reservas que contengan esa fecha exacta
                    if (!(startDate.equals(reserva.getStartTime().toLocalDate()) ||
                          startDate.equals(reserva.getFinishTime().toLocalDate()) ||
                          (startDate.isAfter(reserva.getStartTime().toLocalDate()) &&
                            startDate.isBefore(reserva.getFinishTime().toLocalDate())))) {
                        match = false;
                    }
                }

                if (match) {
                    result.add(reserva);
                }
            }
        }

        return result;
    }

    public java.util.List<Reservation> searchByCliente(String clienteId) {
        return searchReservations(clienteId, null, null);
    }

    public java.util.List<Reservation> searchByDate(LocalDate date) {
        return searchReservations(null, date, null);
    }

    public java.util.List<Reservation> searchByRange(LocalDate startDate, LocalDate endDate) {
        return searchReservations(null, startDate, endDate);
    }

    public java.util.List<Reservation> searchByClienteAndDate(String clienteId, LocalDate date) {
        return searchReservations(clienteId, date, null);
    }

    public java.util.List<Reservation> searchByClienteAndRange(String clienteId, LocalDate startDate, LocalDate endDate) {
        return searchReservations(clienteId, startDate, endDate);
    }
    
    public boolean isVehicleAvailable(String placa, LocalDateTime start, LocalDateTime end) {
        Queue<Reservation> cola = map.get(placa);

        if (cola == null || cola.isEmpty()) {
            return true;
        }

        for (Reservation r : cola) {
            LocalDateTime rStart = r.getStartTime();
            LocalDateTime rEnd   = r.getFinishTime();

            boolean overlap = !rEnd.isBefore(start) && !rStart.isAfter(end);
            if (overlap) {
                return false;
            }
        }

        return true;
    }
    
    public boolean createReservation(Client cliente, Vehicle vehiculo, LocalDateTime start, LocalDateTime finish)
            throws NoClientException, NoCarSelectedException,  InvalidDateException, OverlappingReservationException {
        if (!Reservation.validateClient(cliente.getId())) {
            throw new NoClientException();
        }

        if (!Reservation.validateVehicle(vehiculo.getPlate())) {
            throw new NoCarSelectedException();
        }

        if (!Reservation.validateDates(start, finish)) {
            throw new InvalidDateException();
        }

        if (UtilDate.periodIsValid(start, start)) {
            throw new InvalidDateException();
        }

        if (!isVehicleAvailable(vehiculo.getPlate(), start, finish)) {
            throw new OverlappingReservationException();
        }

        Reservation nueva = new Reservation(cliente, vehiculo, start, finish);
        map.putIfAbsent(vehiculo.getPlate(), new LinkedList<>());
        map.get(vehiculo.getPlate()).add(nueva);
        return true;
    }
    
    public boolean modifyReservationDates(String placa, Reservation reserva, LocalDateTime newStart, LocalDateTime newFinish)
            throws InvalidDateException, OverlappingReservationException {
        Queue<Reservation> cola = map.get(placa);
        if (cola == null || !cola.contains(reserva)) return false;

        if (!reserva.validateDates(newStart, newFinish)) {
            throw new InvalidDateException();
        }

        cola.remove(reserva);
        if (!isVehicleAvailable(placa, newStart, newFinish)) {
            cola.add(reserva);
            throw new OverlappingReservationException();
        }

        reserva.modifyDates(newStart, newFinish);
        cola.add(reserva);
        return true;
    }
    
    public boolean modifyReservationVehicle(String oldPlaca, String newPlaca, Reservation reserva)
            throws NoCarSelectedException, OverlappingReservationException {
        Queue<Reservation> oldQueue = map.get(oldPlaca);
        VehicleList vehicleList = VehicleList.getInstance();
        Vehicle nuevoVehiculo = vehicleList.find(newPlaca);

        if (nuevoVehiculo == null) throw new NoCarSelectedException();

        if (!isVehicleAvailable(newPlaca, reserva.getStartTime(), reserva.getFinishTime())) {
            throw new OverlappingReservationException();
        }

        oldQueue.remove(reserva);
        reserva.modifyCar(nuevoVehiculo);
        map.putIfAbsent(newPlaca, new LinkedList<>());
        map.get(newPlaca).add(reserva);
        return true;
    }
    
    public Reservation confirmReservation(String placa, Reservation reserva) {
        Queue<Reservation> cola = map.get(placa);
        if (cola != null && cola.contains(reserva)) {
            return reserva;
        }
        return null;
    }

    
}
