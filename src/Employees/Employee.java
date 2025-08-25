/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Employees;

import Exceptions.InvalidAgeException;
import Exceptions.InvalidIdException;
import Exceptions.InvalidMailException;
import Exceptions.InvalidPhoneException;
import Exceptions.InvalidSalaryException;
import Persons.Person;
import java.time.LocalDate;

/**
 *
 * @author llean
 */
public class Employee extends Person {
    private EmployeePosition position;
    private Double salary;

    public EmployeePosition getPosition() {
        return position;
    }

    public Double getSalary() {
        return salary;
    }

    public void setPosition(EmployeePosition position) {
        this.position = position;
    }
    
public void setSalary(Double salary) throws InvalidSalaryException {
        if (!validateSalary(salary)){
            throw new InvalidSalaryException();
        }
        this.salary = salary;
    }

    public Employee(String id, String name, LocalDate birthday, String phone, String mail,EmployeePosition position, Double salary) throws InvalidSalaryException, InvalidIdException, InvalidAgeException, InvalidPhoneException, InvalidMailException {
        if (!validateSalary(salary)) {
            throw new InvalidSalaryException();
        }
        super(id, name, birthday, phone, mail);
        this.position = position;
        this.salary = salary;
    }
    
    public static boolean validateSalary(Double salary) {
        return salary > 358600;
    }
}
