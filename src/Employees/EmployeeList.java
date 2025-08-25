/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Employees;

import static Employees.Employee.validateSalary;
import Lists.List;
import java.util.ArrayList;

/**
 *
 * @author llean
 */
public class EmployeeList implements List<Employee> {
    
    ArrayList<Employee> list;
    private static EmployeeList instance;
    
    public static EmployeeList getInstance() {
        if(instance == null){
            instance = new EmployeeList();
        }
        return instance;
    }

    public ArrayList<Employee> getEmployeeList() {
        return list;
    }

    private EmployeeList() {
        this.list = list;
    }
    
    @Override
    public boolean add(Employee t) {
        return list.add(t);
    }
    
    public boolean updateEmployee(String id, String position,Double salary) {
        Employee e = find(id);
        if (e != null) {
            if(position != null && !position.isEmpty()) {
                e.setPosition(position);
            }
            if(salary != null && validateSalary(salary)) {
                e.setSalary(salary);
            }
            return true;
        }
        return false;
    }

    @Override
    public Employee find(Object id) {
        for (Employee e : list) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    @Override
    public boolean remove(Employee t) {
        Employee e = find(t);
        if(e != null) {
            list.remove(e);
            return true;
        }
        return false;
    }
}
