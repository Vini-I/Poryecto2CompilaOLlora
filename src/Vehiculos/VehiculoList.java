/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vehiculos;

import Lists.List;
import java.util.HashMap;

/**
 *
 * @author rodol
 */
public class VehiculoList implements List<Vehiculo> {
    
    HashMap<String, Vehiculo> map;
    private static VehiculoList instance;
    
    public static VehiculoList getInstance(){
        if(instance == null){
            instance = new VehiculoList();
        }
        return instance;
    }

    public HashMap<String, Vehiculo> getMap() {
        return map;
    }

    private VehiculoList() {
        this.map = new HashMap();
    }

    @Override
    public boolean add(Vehiculo t) {
        if (!map.containsKey(t.getPlate())) {
            map.put(t.getPlate(), t);
            return true;
        }
        return false;
    }

    @Override
    public Vehiculo find(Object id) {
        String strId = String.valueOf(id);
        return map.get(strId);
    }

    @Override
    public boolean remove(Vehiculo t) {
        return map.remove(t.getPlate(), map);
    }
    
}
