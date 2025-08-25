/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reservations;

import Lists.List;
import Utils.UtilDate;
import Vehicles.Vehicle;
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
    
}
