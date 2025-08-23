/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Vehicles;

/**
 *
 * @author rodol
 */
public enum VehicleType {
    SEDAN("Sedan"),
    SUV("Suv"),
    PICKUP("Pick-Up"),
    VAN("Van"),
    MINIVAN("Mini Van"),
    MINIBUS("Mini Bus");
    
    private String type;

    public String getType() {
        return type;
    }

    private VehicleType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
    
}
