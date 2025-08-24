/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Exceptions;

/**
 *
 * @author Juan Pablo Rodriguez
 */
public class InvalidAgeExcepcion extends Exception {

    public InvalidAgeExcepcion() {
        super("La edad no puede ser menor a los 18 años");
    }
    
}
