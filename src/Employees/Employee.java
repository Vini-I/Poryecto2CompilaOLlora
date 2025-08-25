/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Employees;

import Exceptions.InvalidSalaryException;
import Persons.Person;
import java.time.LocalDate;

/**
 *
 * @author llean
 */
public class Employee extends Person {
    private String position;
    private Double salary;

    public String getPosition() {
        return position;
    }

    public Double getSalary() {
        return salary;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Employee(String id, String name, LocalDate birthday, String phone, String mail,String position, Double salary) throws InvalidSalaryException {
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
