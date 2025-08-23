/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Vehicles;

/**
 *
 * @author rodol
 */
public enum VehicleState {
    AVAILABLE("Disponible"),
    INMAINTENANCE("Mantenimiento"),
    RENTED("Alquilado");
    
    private String state;

    public String getState() {
        return state;
    }

    private VehicleState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return state;
    }
    
}
