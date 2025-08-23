/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vehiculos;
import Lists.List;
import Utils.UtilDate;
import java.time.LocalDate;

/**
 *
 * @author rodol
 */
public class Vehiculo {
    private String plate;
    private String brand;
    private String model;
    private LocalDate year;
    private TipoVehiculo type;
    private EstadoVehiculo state;

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

    public TipoVehiculo getType() {
        return type;
    }

    public EstadoVehiculo getState() {
        return state;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setType(TipoVehiculo type) {
        this.type = type;
    }

    public void setState(EstadoVehiculo state) {
        this.state = state;
    }

    public Vehiculo(String plate, String brand, String model, LocalDate year, TipoVehiculo type) {
        if(plate.matches("^[\\w]{3}-[1-9]{3}$")){
         this.plate = plate;   
        }
        this.brand = brand;
        this.model = model;
        if(UtilDate.isNotFutureDate(year) && UtilDate.isNotOlderThan20(year)){
         this.year = year;   
        }
        this.type = type;
        this.state = EstadoVehiculo.AVAILABLE;
    }

    @Override
    public String toString() {
        return "Plate: "+ plate + " Brand: " + brand + " Model: " + model + " Year: " + year + " Type: " + type + " State: " + state;
    }
    
    
    
}
