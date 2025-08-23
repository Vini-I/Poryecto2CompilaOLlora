/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Exceptions;

/**
 *
 * @author rodol
 */
public class RentedVehicleException extends Exception {

    public RentedVehicleException() {
        super("El vehiculo no se puede eliminar ya que se encuentra alquilado");
    }
    
}
