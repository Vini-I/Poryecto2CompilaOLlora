/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Contracts;

/**
 *
 * @author autoa
 */
public enum ContractState {
    ACTIVE("Activo"),
    FINALIZED("Finalizado"),
    CANCELED("Cancelado");

    private String state;

    private ContractState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return state;
    }
    
    
    
    
}
