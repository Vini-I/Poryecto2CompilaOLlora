/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Exceptions;

/**
 *
 * @author rodol
 */
public class DuplicatedPlateException extends Exception {

    public DuplicatedPlateException() {
        super("Ya existe un vehiculo con ese numero de placa");
    }
    
}
