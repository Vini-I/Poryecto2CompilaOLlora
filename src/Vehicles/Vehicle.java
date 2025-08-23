/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vehicles;
import Exceptions.InvalidPlateException;
import Exceptions.InvalidYearException;
import Utils.UtilDate;
import java.time.LocalDate;

/**
 *
 * @author rodol
 */
public class Vehicle {
    private String plate;
    private String brand;
    private String model;
    private LocalDate year;
    private VehicleType type;
    private VehicleState state;

    public String getPlate() {
        return plate;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public LocalDate getYear() {
        return year;
    }

    public VehicleType getType() {
        return type;
    }

    public VehicleState getState() {
        return state;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public void setState(VehicleState state) {
        this.state = state;
    }

    public Vehicle(String plate, String brand, String model, LocalDate year, VehicleType type) throws InvalidPlateException, InvalidYearException {
        if(!plate.matches("^[A-Z]{3}-[1-9]{3}$")){
            throw new InvalidPlateException();
        }
        if(!UtilDate.isNotFutureDate(year) || !UtilDate.isNotOlderThan20(year)){
            throw new InvalidYearException();
        }
        this.plate = plate;
        this.year = year;
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.state = VehicleState.AVAILABLE;
    }

    @Override
    public String toString() {
        return "Plate: "+ plate + " Brand: " + brand + " Model: " + model + " Year: " + year + " Type: " + type + " State: " + state;
    }
    
    
    
}
