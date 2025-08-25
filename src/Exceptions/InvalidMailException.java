/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Exceptions;

/**
 *
 * @author rodol
 */
public class InvalidMailException extends Exception {

    public InvalidMailException() {
        super("El correo que usted ingreso no es valido");
    }
    
}
