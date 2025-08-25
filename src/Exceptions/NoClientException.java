/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Exceptions;

/**
 *
 * @author Brwni
 */
public class NoClientException extends Exception {

    public NoClientException() {
        super("El cliente seleccionado no existe o no se ha seleccionado un cliente");
    }
    
}
