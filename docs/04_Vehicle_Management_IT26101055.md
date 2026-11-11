# 🚙 Module 04: Vehicle Fleet Management System
> **Coursework:** SLIIT – Object-Oriented Programming (OOP)  
> **Student Name:** Senevirathna M.G.T.R (Thisara Rukshan)  
> **Student ID:** `IT26101055`  
> **Allocated Role:** Vehicle Fleet & Maintenance Lead  
> **Git Feature Branch:** `IT26101055`  

---

## 📌 1. Module Overview
The **Vehicle Fleet Management Module** oversees the physical automobile assets utilized during student road driving lessons. It tracks registration numbers, vehicle models, transmission types (Automatic vs. Manual), vehicle category (Car, Dual Purpose, Motorcycle, Heavy Truck), and real-time maintenance statuses.

---

## 📂 2. Key Files Contributed
| File Path | Component Type | Responsibility |
|:---|:---|:---|
| `src/main/java/lk/drivingschool/model/Vehicle.java` | Domain Entity | Encapsulates vehicle properties, plate number, type, transmission, status. |
| `src/main/java/lk/drivingschool/servlet/VehicleServlet.java` | Controller | CRUD actions, fleet filtering, status transition handlers. |
| `src/main/webapp/WEB-INF/views/vehicles.jsp` | View Layer (JSP) | Fleet cards, registration modal, maintenance toggles. |
| `data/vehicles.txt` | File Database | Pipe-delimited records for driving school vehicles. |

---

## 💡 3. OOP Concepts & State Pattern

### Encapsulated Model Design (`Vehicle.java`):
```java
public class Vehicle {
    private String id;
    private String registrationNumber;
    private String model;
    private String category;
    private String transmission;
    private String status;

    public Vehicle(String id, String registrationNumber, String model, 
                   String category, String transmission, String status) {
        this.id = id;
        this.registrationNumber = registrationNumber;
        this.model = model;
        this.category = category;
        this.transmission = transmission;
        this.status = status;
    }

    // Public getters and setters with validation
    public boolean isAvailableForLesson() {
        return "AVAILABLE".equalsIgnoreCase(this.status);
    }
}
```

### Operational State Lifecycle:
1. `AVAILABLE`: Ready to be booked for student practical lessons.
2. `IN_LESSON`: Currently on the road with an assigned instructor and student.
3. `MAINTENANCE`: Under routine servicing or repair; temporarily blocked from scheduling.

---

## 🧪 4. Testing & Viva Walkthrough
1. **Navigate to:** `http://localhost:8080/DrivingSchoolSystem/vehicles`
2. **Add a Vehicle:** Click **"Add New Vehicle"**:
   * **Registration Number:** `WP CAD-4567`
   * **Model:** `Toyota Prius 2022`
   * **Category:** `Car (Auto)`
   * **Transmission:** `AUTOMATIC`
   * **Status:** `AVAILABLE`
3. Click **"Save Vehicle"** and verify that it renders in the table with a green badge.
4. Toggle status to `MAINTENANCE` and verify that the badge changes to warning red/amber.

---

## 🎯 5. Expected Viva Questions & Answers
* **Q1: Why is encapsulation critical in the Vehicle class?**  
  * *Answer:* To safeguard vehicle statuses and registration numbers against unintended direct modification, ensuring state transitions (e.g. `AVAILABLE` to `IN_LESSON`) occur via validated controller methods.
* **Q2: How does your module integrate with Lesson Scheduling?**  
  * *Answer:* When scheduling a driving lesson, the system validates whether the assigned vehicle's status is `AVAILABLE`, preventing double-booking during maintenance.
