/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Exceptions;

/**
 *
 * @author Juan Pablo Rodriguez
 */
public class RegisterClientsException extends Exception {

    public RegisterClientsException() {
        super("No se puede registrar cliente sin número de licencia");
    }

    
    
}
