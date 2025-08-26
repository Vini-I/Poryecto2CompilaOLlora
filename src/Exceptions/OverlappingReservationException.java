/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Exceptions;

/**
 *
 * @author autoa
 */
public class OverlappingReservationException extends Exception {

    public OverlappingReservationException() {
        super("Ya hay una reservacion en esas fechas");
    }
    
}
