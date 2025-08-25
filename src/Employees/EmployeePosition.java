/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Employees;

/**
 *
 * @author llean
 */
public enum EmployeePosition {
    MANAGER("Gerente"),
    ACCOUNTANT("Contador"),
    HUMANRESOURCES("Recursos Humanos"),
    RESERVATIONAGENT("Agente de Reservas"),
    CUSTOMERSERVICE("Servicio al Cliente"),
    DRIVER("Chofer"),
    MECHANIC("Mecanico"),
    CARWASHER("Lavador de Vehiculos"),
    IT("IT");
    
    private String position;

    public String getPosition() {
        return position;
    }

    private EmployeePosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return position;
    }
    
    
}
