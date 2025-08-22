/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Vehiculos;

/**
 *
 * @author rodol
 */
public enum TipoVehiculo {
    SEDAN("Sedan"),
    SUV("Suv"),
    PICKUP("Pick-Up"),
    TRUCK("Camion"),
    MOTORCYCLE("Motocicleta");
    
    private String type;

    public String getType() {
        return type;
    }

    private TipoVehiculo(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
    
}
