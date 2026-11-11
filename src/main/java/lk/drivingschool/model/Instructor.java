package lk.drivingschool.model;

/**
 * Instructor Model class.
 * Member 3: Kumarawansha W.K.R.D
 * OOP Concepts:
 * 1. Inheritance: extends Person.
 * 2. Polymorphism: overrides getRole() to return "Instructor".
 * 3. Encapsulation: private licenseCategory and experienceYears with getters/setters.
 */
public class Instructor extends Person {
    private String licenseCategory; // e.g. "Car & Van", "Motorcycle", "Heavy"
    private int experienceYears;    // Used for Bubble Sort!
    private String status;          // "ACTIVE", "ON_LEAVE"

    public Instructor(String id, String name, String email, String phone, String licenseCategory, int experienceYears, String status) {
        super(id, name, email, phone);
        this.licenseCategory = licenseCategory;
        this.experienceYears = experienceYears;
        this.status = status;
    }

    @Override
    public String getRole() {
        return "Instructor";
    }

    public String getLicenseCategory() { return licenseCategory; }
    public void setLicenseCategory(String licenseCategory) { this.licenseCategory = licenseCategory; }

    // Alias for UK spelling support in JSP
    public String getLicenceCategory() { return licenseCategory; }
    public void setLicenceCategory(String licenceCategory) { this.licenseCategory = licenceCategory; }

    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
