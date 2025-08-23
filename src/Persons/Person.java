
package Persons;

import Utils.UtilDate;
import java.time.LocalDate;


public class Person {
    
    public String id;
    public String name; 
    public LocalDate birthday;
    public String phone;
    public String mail;
    public  int years;

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


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMail(String mail) {
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
    
    public Person(String id, String name, LocalDate birthday, String phone, String mail) {
        if(validateId(id))
        throw new IllegalArgumentException("Invalid ID: " + id);
            this.id = id;
        if( name != null && !name.trim().isEmpty())  
            this.name = name;
        if(UtilDate.isNotFutureDate(birthday))
            this.birthday = birthday;
        this.years =UtilDate.calculateAge(birthday);
        if(validatePhone(phone))
            this.phone = phone;
        if(validateMail(mail))
        this.mail = mail;   
    }

    @Override
    public String toString() {
        return "id=" + id + ", name=" + name + ", birthday=" + birthday + ", phone=" + phone + ", mail=" + mail + ", years=" + years + '}';
    }
    
    
    
}
