package lk.drivingschool.model;

/**
 * Student Model class.
 * Member 1: Dinushan R.K
 * OOP Concepts:
 * 1. Inheritance: extends Person.
 * 2. Polymorphism: overrides getRole() to return "Student".
 * 3. Encapsulation: private licenseType and address with getters/setters.
 */
public class Student extends Person {
    private String licenseType; // e.g. "Auto Car (Class B)", "Manual Car", "Motorcycle"
    private String address;

    public Student(String id, String name, String email, String phone, String licenseType, String address) {
        super(id, name, email, phone);
        this.licenseType = licenseType;
        this.address = address;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    public String getLicenseType() { return licenseType; }
    public void setLicenseType(String licenseType) { this.licenseType = licenseType; }

    // Alias for UK spelling support in JSP
    public String getLicenceType() { return licenseType; }
    public void setLicenceType(String licenceType) { this.licenseType = licenceType; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
