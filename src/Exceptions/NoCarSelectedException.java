/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Exceptions;

/**
 *
 * @author Brwni
 */
public class NoCarSelectedException extends Exception{
    
    public NoCarSelectedException() {
        super("El vehiculo seleccionado no existe o no se ha seleccionado un vehiculo");
    }
    
}
