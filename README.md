# SKU-GROUP22-Driving-school
🎓 Member 1: Student Management Subsystem
Object-Oriented Programming University Project – Group 22
Project: Driving School Management and Scheduling System
Assigned Student: Dinushan R.K (IT26101058)
Module: Student Management
📌 Module Responsibilities & Core Features
The Student Management Subsystem maintains learner records for the driving school. Administrators can register new students, view and search their details, update information, and delete records through a JSP web interface.
🧑‍🎓 Student Registration
- Registers a student with full name, email, phone number, driving licence category, and address.
- Automatically creates a student ID such as STU-A1B2C3 using Java's UUID.
- Stores data in the local text file data/students.txt.
🔎 Student Search & Records
- Displays students in a table.
- Allows case-insensitive searching by student ID, name, or email.
- Loads existing student information into an edit form.
📝 Student Information Management
- Updates an existing student's name, email, phone, licence category, and address.
- Deletes a student record by ID.
- Uses Java Servlet request handling and JSP for the web interface.
💾 Local File Persistence
- Uses FileHandler with Java BufferedReader and BufferedWriter.
- Stores one record per line, with the pipe character | separating fields.
- File operations are implemented as synchronized static methods to coordinate access within one Java application process.
🔄 Comprehensive CRUD Operations
Operation	Action / Feature	Actual Java Method	Web Request	Storage
CREATE	Register a student	DataService.addStudent(Student s)	POST /students with action=create	Append to data/students.txt
READ	List all students	DataService.getStudents()	GET /students	Read file
READ	Search by ID, name, email	StudentServlet.doGet() filtering	GET /students?q=...	Filter loaded records
READ	Find student by ID	DataService.findStudentById(String id)	GET /students?action=edit&id=...	Read file, load edit form
UPDATE	Edit student information	DataService.updateStudent(Student s)	POST /students with action=update	Rewrite updated records
DELETE	Remove student	DataService.deleteStudent(String id)	POST /students with action=delete	Rewrite remaining records


Note: The servlet is mapped to both /student and /students. The URL paths above refer to the application context (for example, /DrivingSchoolSystem/students). This is a Jakarta Servlet/JSP application, not a Spring Boot REST API.
💡 Detailed Breakdown of CRUD Methods
1. 🟢 CREATE – Register New Student
The servlet reads form values, generates an ID, creates a Student object, and calls the service method to save it.
String id = "STU-" + UUID.randomUUID()
        .toString().substring(0, 6).toUpperCase();

Student student = new Student(
    id,
    "Kamal Perera",
    "kamal@example.com",
    "0771234567",
    "Auto Car (Class B)",
    "Colombo"
);

DataService.addStudent(student);
Flow: JSP form → StudentServlet.doPost() → DataService.addStudent() → FileHandler.append() → students.txt.
2. 🔵 READ – Display and Search Students
List<Student> students = DataService.getStudents();
Student student = DataService.findStudentById("STU-1001");
For search, the servlet checks whether each student's ID, name, or email contains the search text, ignoring case.
GET /students?q=kamal
Flow: Browser request → StudentServlet.doGet() → DataService.getStudents() → FileHandler.load() → JSP table.
3. 🟡 UPDATE – Edit Student Details
Student student = DataService.findStudentById("STU-1001");
if (student != null) {
    student.setPhone("0712345678");
    DataService.updateStudent(student);
}
FileHandler.update() finds the record with the matching student ID, replaces the row, and saves the updated file.
4. 🔴 DELETE – Remove Student
DataService.deleteStudent("STU-1001");
FileHandler.delete() removes the matching ID from the loaded rows and saves the remaining records. The JSP has a confirmation prompt before submitting the delete action.

Important: The provided code does not implement automatic cascading deletion of this student's lessons, payments, or feedback.
🗂️ Student Data Storage Format
File: data/students.txt
STU-1001|Kamal Perera|kamal@example.com|0771234567|Auto Car (Class B)|Colombo
Field Position	Field	Example
1	Student ID	STU-1001
2	Full Name	Kamal Perera
3	Email	kamal@example.com
4	Phone	0771234567
5	Licence Type	Auto Car (Class B)
6	Address	Colombo


This is a plain-text, pipe-delimited file, not MySQL or a CSV file. FileHandler also creates sample data when the file has no records.
🏛️ OOP Principles Implementation
OOP Concept	Implementation	Simple Viva Explanation
Abstraction	Abstract class Person defines getRole()	The parent describes a common contract while subclasses specify their role.
Inheritance	Student extends Person	Student reuses common fields such as ID, name, email, and phone.
Polymorphism	Student overrides getRole()	Calling getRole() on a Student object gives "Student", even when referenced as a Person.
Encapsulation	Private fields and getters/setters	Student data is accessed through public methods rather than directly.


Parent and Child Classes
public abstract class Person {
    private String id;
    private String name;
    private String email;
    private String phone;

    public abstract String getRole();
    // Constructor, getters, and setters exist in the source.
}
public class Student extends Person {
    private String licenseType;
    private String address;

    public Student(String id, String name, String email,
                   String phone, String licenseType, String address) {
        super(id, name, email, phone);
        this.licenseType = licenseType;
        this.address = address;
    }

    @Override
    public String getRole() {
        return "Student";
    }
    // Getters and setters exist in the source.
}
Runtime Polymorphism Example
Person person = new Student(
    "STU-1001", "Kamal", "kamal@example.com",
    "0771234567", "Auto Car (Class B)", "Colombo"
);
System.out.println(person.getRole());
// Output: Student
🏗️ Module Architecture
Browser / students.jsp
         |
         v
StudentServlet.java (Controller)
         |
         v
DataService.java (Business / CRUD Service)
         |
         v
FileHandler.java (File Access)
         |
         v
data/students.txt (Persistent Storage)
The Student model represents one student. The Person class is its abstract parent. The service connects the controller to storage.
📂 Main Source Files
TempDriver-master/
├── pom.xml
├── data/
│   └── students.txt
└── src/main/
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
DataService and FileHandler are shared with other project modules.
🧪 Testing & Execution
