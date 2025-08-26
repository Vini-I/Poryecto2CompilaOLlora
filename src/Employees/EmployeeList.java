/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Employees;

import static Employees.Employee.validateSalary;
import Exceptions.InvalidMailException;
import Exceptions.InvalidPhoneException;
import Exceptions.InvalidSalaryException;
import Lists.List;
import java.time.LocalDate;
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
        this.list = new ArrayList();
    }
    
    @Override
    public boolean add(Employee t) {
        return list.add(t);
    }
    
    public boolean updateEmployee(String id, String phone, String mail,EmployeePosition position, Double salary) throws InvalidPhoneException, InvalidMailException, InvalidSalaryException {
        Employee e = find(id);
        if (e != null) {
                e.setPhone(phone);
                e.setMail(mail);
                e.setPosition(position);
                e.setSalary(salary);
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
           return list.remove(t);
    }
}
