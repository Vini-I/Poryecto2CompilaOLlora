/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Exceptions;

/**
 *
 * @author Juan Pablo Rodriguez
 */
public class InvalidAgeException extends Exception {

    public InvalidAgeException() {
        super("La edad no puede ser menor a los 18 años o una fecha futura");
    }
    
}
