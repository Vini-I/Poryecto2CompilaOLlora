
package Clients;

import Persons.Person;
import java.time.LocalDate;


public class Client extends Person {

   private LicenseType licenseType;
   private String numberLicense;

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public String getNumberLicense() {
        return numberLicense;
    }

    public void setLicenseType(LicenseType licenseType) {
        this.licenseType = licenseType;
    }

    public void setNumberLicense(String numberLicense) {
        this.numberLicense = numberLicense;
    }
    
    public static boolean validateNumberLicense(String numberLicense){
       return numberLicense.matches("^(DM|CI)-[1-7]0\\d{3}0\\d{3}$");    
    }

    public Client(LicenseType licenseType, String numberLicense, String id, String name, LocalDate birthday, int years, String phone, String mail) {
        super(id, name, birthday, years, phone, mail);
        this.licenseType = LicenseType.B1;
        if(validateNumberLicense(numberLicense))
            this.numberLicense = numberLicense;
    }  
}
