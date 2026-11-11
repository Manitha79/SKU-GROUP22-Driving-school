# 🎓 Module 01: Student Management System
> **Coursework:** SLIIT – Object-Oriented Programming (OOP)  
> **Student Name:** Dinushan R.K  
> **Student ID:** `IT26101058`  
> **Allocated Role:** Student Management Component Lead  
> **Git Feature Branch:** `student-it26101058`  

---

## 📌 1. Module Overview
The **Student Management Module** is the foundational entry point of the Apex Driving School system. It is responsible for handling end-to-end student lifecycles including:
* Registration of new driving students.
* Capturing student contact, license category requirements, address, and emergency contact details.
* Modification of existing student profiles.
* Safe removal and listing of active students.
* Data persistence in local file storage (`data/students.txt`).

---

## 📂 2. Key Files Contributed
| File Path | Component Type | Responsibility |
|:---|:---|:---|
| `src/main/java/lk/drivingschool/model/Person.java` | Base Model (Superclass) | Encapsulates shared attributes (`id`, `name`, `email`, `phone`). |
| `src/main/java/lk/drivingschool/model/Student.java` | Subclass Model | Extends `Person`, adding `licenceType`, `address`, `emergencyContact`. |
| `src/main/java/lk/drivingschool/servlet/StudentServlet.java` | Controller | Handles `doGet` (listing/search) and `doPost` (Create/Update/Delete). |
| `src/main/webapp/WEB-INF/views/students.jsp` | View Layer (JSP) | Modern table view, student registration modal, status badges. |
| `data/students.txt` | File Database | Pipe-delimited flat-file storage for student records. |

---

## 💡 3. OOP Concepts Implemented

### A. Inheritance (IS-A Relationship)
`Student` inherits directly from the abstract/base class `Person`:
```java
public class Student extends Person {
    private String licenceType;
    private String address;
    private String emergencyContact;

    public Student(String id, String name, String email, String phone, 
                   String licenceType, String address, String emergencyContact) {
        super(id, name, email, phone); // Reusing superclass constructor
        this.licenceType = licenceType;
        this.address = address;
        this.emergencyContact = emergencyContact;
    }
}
```

### B. Polymorphism (Method Overriding)
Overrides the `getRole()` method from `Person`:
```java
@Override
public String getRole() {
    return "STUDENT";
}
```
At runtime, whenever `person.getRole()` is invoked, dynamic method dispatch ensures that `"STUDENT"` is returned for student instances.

### C. Encapsulation
* All fields (`licenceType`, `address`, `emergencyContact`, `id`, `name`) are declared `private`.
* Access is provided solely through public getters and setters with validation rules.

---

## 🧪 4. Testing & Viva Walkthrough
1. **Navigate to:** `http://localhost:8080/DrivingSchoolSystem/students`
2. **Add a Student:** Click **"Register Student"**, fill in:
   * **Name:** `Kasun Wickramasinghe`
   * **Email:** `kasun.w@gmail.com`
   * **Phone:** `0771239988`
   * **License Type:** `Auto Car (Class B)`
   * **Emergency Contact:** `0718822334`
3. Click **"Save Student"** and verify persistence in `data/students.txt`.

---

## 🎯 5. Expected Viva Questions & Answers
* **Q1: Where did you use Inheritance in your code?**  
  * *Answer:* In `Student.java`, which extends `Person.java`. Common attributes (`id`, `name`, `email`, `phone`) are inherited from the parent class using `super()`.
* **Q2: Why use private fields in the Student model?**  
  * *Answer:* To enforce encapsulation, ensuring data integrity so fields cannot be modified directly without passing through business validation.
