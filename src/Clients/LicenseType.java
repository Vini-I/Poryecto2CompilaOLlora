
package Clients;


public enum LicenseType {
    
    B1("B1"),
    B2("B2"),
    B3("B3");
    
    private String LicenseType;

    public String getLicenseType() {
        return LicenseType;
    }

    public void setLicenseType(String LicenseType) {
        this.LicenseType = LicenseType;
    }

    private LicenseType(String LicenseType) {
        this.LicenseType = LicenseType;
    }

    @Override
    public String toString() {
        return LicenseType;
    }

    
    
    
    
    
    
    
}
