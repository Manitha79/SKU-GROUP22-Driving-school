# 🎓 Student Registration & Management Module

**Apex Driving School System — SLIIT Object-Oriented Programming (OOP) Coursework**

| Detail | Information |
|---|---|
| Academic Year | 2026 |
| Group ID | SKU-GROUP 22 |
| Student Name | Dinushan R.K |
| Student ID | IT26101058 |
| Project Role | Student Management Component Lead (Member 01) |
| Technology Stack | Java 17, Jakarta Servlet, JSP, HTML, CSS, Maven, Apache Tomcat |

## 📌 1. Module Overview

The **Student Registration & Management Module** maintains student information in the Apex Driving School application. It provides a web interface for registering students, viewing records, searching existing students, modifying details, and deleting records.

### Core Features

- **Student Registration:** Register students with name, email, phone number, driving licence type and address.
- **Automatic Student ID Generation:** New IDs use the prefix `STU-` followed by six characters derived from a random UUID.
- **Student Directory:** Display student records through a JSP interface.
- **Search:** Filter student records by student ID, name or email.
- **Edit and Delete:** Update registered student information or remove a record.
- **File-Based Persistence:** Read and write pipe-delimited records in `data/students.txt`.
- **Object-Oriented Structure:** Apply abstraction, inheritance, polymorphism and encapsulation using `Person` and `Student`.

## 📂 2. Files & Architecture

The module follows a **Model–View–Controller (MVC)** structure supported by service and file-handling layers.

```text
src/main/
├── java/lk/drivingschool/
│   ├── model/
│   │   ├── Person.java
│   │   └── Student.java
│   ├── servlet/
│   │   └── StudentServlet.java
│   ├── service/
│   │   └── DataService.java
│   └── util/
│       └── FileHandler.java
└── webapp/WEB-INF/views/
    └── students.jsp

data/
└── students.txt
```

> **Note:** `DataService.java` and `FileHandler.java` are shared infrastructure files used by multiple modules. The Student Management functionality uses their student-specific CRUD and file-handling operations.

| File | Layer | Responsibility |
|---|---|---|
| `Person.java` | Abstract model | Common ID, name, email and phone fields; abstract `getRole()` method |
| `Student.java` | Model | Student-specific licence type and address; extends `Person` |
| `StudentServlet.java` | Controller | Receives HTTP GET/POST requests, handles CRUD actions and forwards to JSP |
| `DataService.java` | Service | Student CRUD methods and student lookup |
| `FileHandler.java` | Persistence utility | Loads, appends, updates and deletes pipe-delimited file records |
| `students.jsp` | View | Student listing, registration and editing interface |
| `students.txt` | Data | Local persistent student records |

## 💡 3. OOP Concepts

### A. Abstraction

The abstract `Person` class defines shared personal details and the `getRole()` contract.

```java
public abstract class Person {
    private String id;
    private String name;
    private String email;
    private String phone;

    public abstract String getRole();
    // Constructor and accessors are defined in Person.java.
}
```

### B. Inheritance

`Student` inherits the common data and behaviour of `Person` and adds student-specific information.

```java
public class Student extends Person {
    private String licenseType;
    private String address;

    public Student(String id, String name, String email,
                   String phone, String licenseType, String address) {
        super(id, name, email, phone);
        this.licenseType = licenseType;
        this.address = address;
    }
}
```

### C. Polymorphism

The `Student` class overrides `getRole()`, providing a student-specific implementation of the abstract method.

```java
@Override
public String getRole() {
    return "Student";
}
```

### D. Encapsulation

Student attributes are private, with public getters and setters controlling access.

```java
private String licenseType;

public String getLicenseType() {
    return licenseType;
}

public void setLicenseType(String licenseType) {
    this.licenseType = licenseType;
}
```

## 🔄 4. CRUD Operations

| Operation | Service Method | HTTP Route | Storage Action |
|---|---|---|---|
| **CREATE** | `DataService.addStudent(Student s)` | `POST /students` (`action=create`) | Append a record to `students.txt` |
| **READ** | `DataService.getStudents()` | `GET /students` | Load and display records |
| **SEARCH** | `DataService.findStudentById(String id)` / controller filter | `GET /students?q=...` | Search loaded records by ID, name or email |
| **UPDATE** | `DataService.updateStudent(Student s)` | `POST /students` (`action=update`) | Replace matching record by student ID |
| **DELETE** | `DataService.deleteStudent(String id)` | `POST /students` (`action=delete`) | Remove matching record by student ID |

### Create: Register a Student

```java
public static void addStudent(Student s) {
    String[] row = new String[]{
        s.getId(), s.getName(), s.getEmail(),
        s.getPhone(), s.getLicenseType(), s.getAddress()
    };
    FileHandler.append("students.txt", row);
}
```

### Read: Retrieve Students

```java
public static List<Student> getStudents() {
    List<Student> list = new ArrayList<>();
    for (String[] r : FileHandler.load("students.txt")) {
        if (r.length >= 6) {
            list.add(new Student(r[0], r[1], r[2], r[3], r[4], r[5]));
        }
    }
    return list;
}
```

### Update: Modify Student Details

```java
public static void updateStudent(Student s) {
    String[] row = new String[]{
        s.getId(), s.getName(), s.getEmail(),
        s.getPhone(), s.getLicenseType(), s.getAddress()
    };
    FileHandler.update("students.txt", s.getId(), row);
}
```

### Delete: Remove a Student

```java
public static void deleteStudent(String id) {
    FileHandler.delete("students.txt", id);
}
```

## 💾 5. File-Based Data Persistence

Student records are stored locally in `data/students.txt` using a pipe (`|`) separator.

**Record format:**

```text
STUDENT_ID|NAME|EMAIL|PHONE|LICENSE_TYPE|ADDRESS
```

**Example:**

```text
STU-1001|Kamal Perera|kamal@example.com|0771234567|Auto Car (Class B)|Colombo
```

`FileHandler` handles file operations using Java buffered I/O, and its synchronized methods coordinate access among threads within the same Java application process. Because the data directory is a relative path, the effective file location depends on the server's working directory.

## 🌐 6. Frontend–Backend Integration

The application uses **JSP forms and Jakarta Servlets**, not a separate REST frontend framework.

```text
Browser (students.jsp)
        |
        | HTTP GET / POST
        v
Apache Tomcat
        |
        v
StudentServlet.java
        |
        v
DataService.java
        |
        v
FileHandler.java
        |
        v
data/students.txt
```

The controller is mapped to both `/student` and `/students`:

```java
@WebServlet(urlPatterns = {"/student", "/students"})
public class StudentServlet extends HttpServlet {
    // doGet(...) and doPost(...) handle student requests.
}
```

- `doGet()` loads the student list, applies an optional search filter and forwards data to `students.jsp`.
- `doPost()` handles create, update and delete actions based on the form's `action` parameter.
- After successful submission, the browser is redirected to `/students?success=true`.

## 🧪 7. Build and Run

### Prerequisites

- JDK 17 or newer (compatible JDK recommended)
- Apache Maven
- Apache Tomcat 10.1+
- IntelliJ IDEA or another Java IDE (optional)

### Build

From the project root:

```bash
mvn clean package
```

Maven creates:

```text
target/DrivingSchoolSystem.war
```

### Deploy

1. Copy `DrivingSchoolSystem.war` into Tomcat's `webapps/` directory.
2. Start the Tomcat server.
3. Open the student module in the browser:

```text
http://localhost:8080/DrivingSchoolSystem/students
```

The URL assumes the default port `8080` and the default WAR context path. Adjust these if your Tomcat configuration differs.

## 🧩 8. Module Integration

The Student Management module works with the rest of the Driving School Management and Scheduling System. Student IDs can be referenced by other modules, including lesson scheduling and payments. It uses shared services and file-handling utilities within the overall application.

## 👨‍💻 9. Contributor

**Dinushan R.K**  
**Student ID:** IT26101058  
**Group:** SKU-GROUP 22  
**Component:** Student Registration & Management  
**Academic Year:** 2026

---

*Student Management component documentation for the Apex Driving School System OOP coursework.*

