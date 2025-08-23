/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Exceptions;

/**
 *
 * @author rodol
 */
public class InvalidPlateException extends Exception {

    public InvalidPlateException() {
        super("El formato de la placa no es valido");
    }
    
}
