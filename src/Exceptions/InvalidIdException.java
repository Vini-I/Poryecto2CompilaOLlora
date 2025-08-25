/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Exceptions;

/**
 *
 * @author rodol
 */
public class InvalidIdException extends Exception {

    public InvalidIdException() {
        super("Formato de cedula no valido. Intente agregar los ceros si no lo esta haciendo");
    }
    
}
