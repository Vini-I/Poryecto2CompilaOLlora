
package Clients;



import Exceptions.InvalidAgeException;
import Exceptions.InvalidIdException;
import Exceptions.InvalidLicenseExcepcion;
import Exceptions.InvalidMailException;
import Exceptions.InvalidPhoneException;
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

    public void setNumberLicense(String numberLicense) throws InvalidLicenseExcepcion {
        if(!validateNumberLicense(numberLicense)) {
            throw new InvalidLicenseExcepcion();
        }
        this.numberLicense = numberLicense;
    }
    
    public static boolean validateNumberLicense(String numberLicense){
       return numberLicense.matches("^(DM|CI)-[1-7]0\\d{3}0\\d{3}$");    
    }

    public Client(String id, String name, LocalDate birthday, String phone, String mail, LicenseType licenseType, String numberLicense) throws InvalidIdException, InvalidAgeException, InvalidPhoneException, InvalidMailException, InvalidLicenseExcepcion {
        super(id, name, birthday, phone, mail);
        this.licenseType = licenseType;
        if (!validateNumberLicense(numberLicense)){
            throw new InvalidLicenseExcepcion();
        }
        this.numberLicense = numberLicense;
    }

    @Override
    public String toString() {
        return "Client{" + "id= " +id +"name+ "+ name +"birthday= " +birthday +"years= "+ getYears()+ "phone= "+ phone + "mail= "+mail +"licenseType= "+ licenseType  +"numberLicense ="+ numberLicense + '}';
    }

   
        
}
