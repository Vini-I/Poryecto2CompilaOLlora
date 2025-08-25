
package Persons;

import Exceptions.InvalidAgeException;
import Exceptions.InvalidIdException;
import Exceptions.InvalidMailException;
import Exceptions.InvalidPhoneException;
import Utils.UtilDate;
import java.time.LocalDate;


public abstract class Person {
    
    public String id;
    public String name; 
    public LocalDate birthday;
    public String phone;
    public String mail;
    public int years;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }

    public int getYears() {
        return years;
    }

    public void setPhone(String phone) throws InvalidPhoneException {
        if (!validatePhone(phone)) {
            throw new InvalidPhoneException();
        }
        this.phone = phone;
    }

    public void setMail(String mail) throws InvalidMailException {
        if (!validateMail(mail)){
             throw new InvalidMailException();
        }
        this.mail = mail;
    }

    private static boolean validateId(String Id){
        return Id.matches("^[1-7]0\\d{3}0\\d{3}$");
    }
    
    private static boolean validatePhone(String phone){
        return phone.matches("^[6-8]\\d{7}$");
    }
            
    private static boolean validateMail(String mail){
        return mail.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,6}$");  
    }
    
    public Person(String id, String name, LocalDate birthday, String phone, String mail) throws InvalidIdException, InvalidAgeException , InvalidPhoneException, InvalidMailException{
        if (!validateId(id)) {
            throw new InvalidIdException();
        }
        if (!UtilDate.isNotFutureDate(birthday) || !UtilDate.isLegalAge(birthday)) {
            throw new InvalidAgeException();
        }
        if (!validatePhone(phone)) {
            throw new InvalidPhoneException();
        }

        if (!validateMail(mail)) {
            throw new InvalidMailException();
        }
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.phone = phone;
        this.mail = mail;
        this.years = UtilDate.calculateAge(birthday);
    }

    // probar funcionalidad en consola
    @Override
    public String toString() {
        return "id " + id + " name " + name + " birthday " + birthday + " phone " + phone + " mail " + mail + " years " + years;
    }
    
    
    
}
