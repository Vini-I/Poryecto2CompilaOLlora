/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vehicles;

import Lists.List;
import java.util.HashMap;


/**
 *
 * @author rodol
 */
public class VehicleList implements List<Vehicle> {
    
    HashMap<String, Vehicle> map;
    private static VehicleList instance;
    
    public static VehicleList getInstance(){
        if(instance == null){
            instance = new VehicleList();
        }
        return instance;
    }

    public HashMap<String, Vehicle> getMap() {
        return map;
    }

    private VehicleList() {
        this.map = new HashMap();
    }

    @Override
    public boolean add(Vehicle t) {
            return map.put(t.getPlate(), t) == null;
    }

    @Override
    public Vehicle find(Object id) {
        String strId = String.valueOf(id);  
        return map.get(strId);
    }

    @Override
    public boolean remove(Vehicle t){
        return map.remove(t.getPlate()) != null;
    }
}
