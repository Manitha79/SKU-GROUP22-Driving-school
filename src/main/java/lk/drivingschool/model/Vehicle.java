package lk.drivingschool.model;

/**
 * Vehicle Model class.
 * Member 4: Senevirathna M.G.T.R
 * OOP Concepts:
 * 1. Encapsulation: Private fleet attributes with getters and setters.
 * 2. State Lifecycle: status can be "AVAILABLE", "IN_LESSON", or "MAINTENANCE".
 */
public class Vehicle {
    private String id;
    private String plateNumber;
    private String model;
    private String category;     // "Car (Auto)", "Car (Manual)", "Motorbike", "Van"
    private String transmission; // "AUTOMATIC", "MANUAL"
    private String status;       // "AVAILABLE", "IN_LESSON", "MAINTENANCE"

    public Vehicle(String id, String plateNumber, String model, String category, String transmission, String status) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.model = model;
        this.category = category;
        this.transmission = transmission;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }

    // Alias for JSP
    public String getRegistrationNo() { return plateNumber; }
    public void setRegistrationNo(String regNo) { this.plateNumber = regNo; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) { this.transmission = transmission; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
