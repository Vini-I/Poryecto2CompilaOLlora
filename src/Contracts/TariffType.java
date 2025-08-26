/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Contracts;

/**
 *
 * @author autoa
 */
public enum TariffType {
    
    SEDAN(15000),
    SUV(25000),
    PICKUP(30000),
    VAN(40000),
    MINIVAN(30000),
    MINIBUS(50000);
    
    private int tarifa;

    public int getTarifa() {
        return tarifa;
    }

    private TariffType(int tarifa) {
        this.tarifa = tarifa;
    }

    @Override
    public String toString() {
        return Integer.toString(tarifa);
    }
    
    
}
