/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Exceptions;

/**
 *
 * @author llean
 */
public class InvalidSalaryException extends Exception {

    public InvalidSalaryException() {
        super("Salario menor al minimo");
    }
}
