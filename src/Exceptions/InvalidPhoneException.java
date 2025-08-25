/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Exceptions;

/**
 *
 * @author rodol
 */
public class InvalidPhoneException extends Exception{

    public InvalidPhoneException() {
        super("Numero de telefono no valido. Solo se permite numeros empezando por 6 u 8");
    }
    
}
