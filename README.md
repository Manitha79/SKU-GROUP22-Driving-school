@@ -1,264 +1,286 @@
# 🚗 Apex Driving School Management & Scheduling System
> **SLIIT – Object-Oriented Programming (OOP) Coursework Project**  
> **Group ID:** SKU-GROUP 22 | **Academic Year:** 2026  

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Jakarta EE](https://img.shields.io/badge/Jakarta_EE-10-F80000?style=for-the-badge&logo=jakartaee&logoColor=white)](https://jakarta.ee/)
[![Apache Maven](https://img.shields.io/badge/Apache_Maven-3.8%2B-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![Apache Tomcat](https://img.shields.io/badge/Apache_Tomcat-10.1%2B-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=black)](https://tomcat.apache.org/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)](https://getbootstrap.com/)

---

## 📌 1. Project Overview

**Apex Driving School System** is a unified, enterprise-grade Java web application designed to automate and streamline the end-to-end operations of a modern driving academy. Built strictly under the **Model-View-Controller (MVC)** architectural pattern, it leverages **Jakarta EE 10 Servlets**, **JavaServer Pages (JSP)**, **JSTL**, and **robust file-based persistence** without requiring an external SQL database server.

The application connects six core business modules into a cohesive ecosystem featuring role-based authentication, interactive KPI dashboards, real-time input validation, and dedicated implementations of fundamental Object-Oriented Programming (OOP) concepts and Data Structures & Algorithms (DSA).

---

## 👥 2. Group Members & Module Allocation

All six group members have contributed dedicated modules developed in separate Git feature branches and integrated into the primary codebase:

| # | Student Name | Student ID (IT No.) | Allocated Module & Role | Git Feature Branch | Dedicated Documentation | Key Contributions & Classes |
|:---:|:---|:---:|:---|:---:|:---:|:---|
| **1** | **Dinushan R.K** | `IT26101058` | **Student Management** | `student-it26101058` | [📖 Module 01 Guide](docs/01_Student_Management_IT26101058.md) | `Student.java`, `Person.java`, `StudentServlet.java`, `students.jsp` |
| **2** | **Thanneliyadda** | `IT26101056` | **Lesson Scheduling & Queue** | `lesson-IT26101056` | [📖 Module 02 Guide](docs/02_Lesson_Scheduling_Queue_IT26101056.md) | `Lesson.java`, `LessonQueue.java` (FIFO Queue), `LessonServlet.java` |
| **3** | **Kumarawansha W.K.R.D** | `IT26101057` | **Instructor Management & Sorting** | `feature/instructor-management` | [📖 Module 03 Guide](docs/03_Instructor_Management_BubbleSort_IT26101057.md) | `Instructor.java`, `BubbleSort.java` (Experience Sorting) |
| **4** | **Senevirathna M.G.T.R** *(Thisara Rukshan)* | `IT26101055` | **Vehicle Fleet Management** | `IT26101055` | [📖 Module 04 Guide](docs/04_Vehicle_Management_IT26101055.md) | `Vehicle.java`, `VehicleServlet.java`, `vehicles.jsp` |
| **5** | **Weerakkody M.R.B** *(Manitha – Group Leader)* | `IT26101059` | **Payment & Billing Management** | `payment-billing` | [📖 Module 05 Guide](docs/05_Payment_Billing_IT26101059.md) | `Payment.java`, `PaymentServlet.java`, `payments.jsp`, Invoicing |
| **6** | **Sajan P.** | `IT26101060` | **Feedback, Dashboard & Security** | `member-6` | [📖 Module 06 Guide](docs/06_Feedback_Admin_IT26101060.md) | `Feedback.java`, `AuthFilter.java`, `dashboard.jsp`, UI Theme |

---

## 🏛️ 3. System Architecture & Design Pattern

The application is structured following the standard **MVC (Model-View-Controller)** architectural pattern to ensure clean separation of concerns:

```
                          ┌──────────────────────────┐
                          │   Client Browser / JSP   │
                          └─────────────┬────────────┘
                                        │ HTTP Requests (GET/POST)
                                        ▼
                          ┌──────────────────────────┐
                          │   AuthFilter (Security)  │
                          └─────────────┬────────────┘
                                        │ Authorized
                                        ▼
                          ┌──────────────────────────┐
                          │  Servlets (Controllers)  │
                          │ Student / Lesson / etc.  │
                          └──────┬────────────┬──────┘
             Reads / Modifies    │            │ Forwards Data (Request Scope)
                                 ▼            ▼
                   ┌──────────────────┐  ┌──────────────────┐
                   │ Models & Services│  │    JSP Views     │
                   │ Person, DataSvc  │  │ JSTL / Bootstrap │
                   └─────────┬────────┘  └──────────────────┘
                             │
                             ▼ File I/O
                   ┌──────────────────┐
                   │ data/*.txt (DB)  │
                   └──────────────────┘
```

* **Model Layer (`lk.drivingschool.model`):** Pure Java Beans representing real-world domain entities (`Student`, `Instructor`, `Lesson`, `Vehicle`, `Payment`, `Feedback`).
* **Controller Layer (`lk.drivingschool.servlet`):** Jakarta HTTP Servlets handling incoming client requests, parsing parameters, executing validations, and delegating to services.
* **Service & Persistence Layer (`lk.drivingschool.service`, `lk.drivingschool.util`):** Centralized data handling via `DataService` and thread-safe `FileHandler` storing records in pipe-delimited text files (`|`).
* **View Layer (`src/main/webapp/WEB-INF/views/`):** JSP presentation layer with modular layout fragments (`layout_header.jspf`, `layout_footer.jspf`), clean CSS variables, responsive tables, and modals.

---

## 💡 4. OOP & DSA Concepts Demonstrated (Viva Reference)

### A. Object-Oriented Programming (OOP) Pillars
1. **Inheritance:**  
   * Base class `Person.java` encapsulates common personal attributes (`id`, `name`, `email`, `phone`).  
   * `Student.java` and `Instructor.java` inherit from `Person`, adding module-specific attributes (`licenceType`, `emergencyContact` vs. `licenseCategory`, `experienceYears`).
2. **Polymorphism:**  
   * The abstract method `getRole()` in `Person` is polymorphically overridden:
     * `Student.getRole()` returns `"STUDENT"`
     * `Instructor.getRole()` returns `"INSTRUCTOR"`
   * `toString()` is dynamically overridden for formatted logging across entities.
3. **Encapsulation:**  
   * Strict `private` field access modifiers across all models.
   * Access is controlled through public getters, setters, and parameter validation routines preventing invalid object states.
4. **Abstraction:**  
   * The file persistence engine abstracts the mechanics of file reading, parsing, splitting, and writing behind clear public interfaces (`DataService.getAllStudents()`, `savePayment()`).

### B. Data Structures & Algorithms (DSA)
1. **Custom FIFO Queue (`LessonQueue.java` – Module 2):**  
   * Implements a First-In-First-Out (FIFO) queue for driving lesson requests.
   * Ensures fair scheduling: student lesson requests are queued based on submission timestamp and processed in exact arrival sequence (`enqueue()`, `dequeue()`, `peek()`).
2. **Optimized Bubble Sort (`BubbleSort.java` – Module 3):**  
   * Custom sorting algorithm used to rank instructors based on their years of driving experience.
   * Features an early-termination optimization flag (`boolean swapped`): achieves $O(n)$ best-case time complexity when already sorted, and $O(n^2)$ worst-case.
   * Supports both descending (senior instructors first) and ascending ordering.

---

## 📦 5. Module Breakdown & Features

### 🎓 Module 1: Student Management (`IT26101058 - Dinushan R.K`)
* Complete CRUD operations (Register, View, Update, Delete) for driving school students.
* Validation for National Identity / Student ID, phone number formatting, and license categories (Class A, B, B1, C).
* Quick search and tabular view with emergency contact linkage.

### 📅 Module 2: Lesson Scheduling & FIFO Queue (`IT26101056 - Thanneliyadda`)
* Lesson booking interface connecting students, instructors, and available time slots.
* Pending lesson bookings automatically enter the **Custom FIFO Queue** (`LessonQueue`).
* Queue status badges: `CONFIRMED`, `PENDING_QUEUE`, `COMPLETED`, `CANCELLED`.
* Dispatch queue controller allowing administrators to confirm queued lessons in chronological order.

### 👨‍🏫 Module 3: Instructor Management & Ranking (`IT26101057 - Kumarawansha`)
* Instructor registry capturing license categories, direct contact information, and teaching tenure.
* **Bubble Sort Algorithm execution**: Interactive toggle to sort instructors by experience (Highest to Lowest / Lowest to Highest).
* Display metrics showing swap counts and execution comparisons for viva demonstrations.

### 🚙 Module 4: Vehicle Fleet Management (`IT26101055 - Senevirathna / Rukshan`)
* Fleet tracking capturing registration numbers, model/make, transmission type (`AUTOMATIC`, `MANUAL`), and vehicle category.
* Real-time operational lifecycle status badges: `AVAILABLE`, `IN_LESSON`, `MAINTENANCE`.
* Quick status updates to prevent double-booking vehicles during active road sessions.

### 💳 Module 5: Payment & Billing Management (`IT26101059 - Weerakkody / Manitha [Leader]`)
* Invoicing and transaction logging linked to student accounts.
* Support for payment methods: `CASH`, `CARD`, `ONLINE_TRANSFER`.
* Real-time financial aggregation calculating Total Revenue, Pending Dues, and Collected Balances.
* Printable invoice simulation and billing status tracking (`PAID`, `PENDING`, `REFUNDED`).

### 📊 Module 6: Executive Dashboard, Feedback & Security (`IT26101060 - Sajan P.`)
* **Executive Dashboard:** Aggregates live KPI cards summarizing key data points from all 6 modules.
* **Student Feedback Engine:** Collects 5-star ratings and reviews with auto-calculated overall satisfaction index.
* **Authentication & Authorization Filter (`AuthFilter`):** Intercepts requests, protects routes, and enforces role-based access for Admin, Instructor, and Student accounts.
* Global UI Design System with unified headers, breadcrumbs, notifications, and responsive styling.

---

## 📂 6. Directory Structure

```
TempDrive/
├── data/                                 # Persistent text databases (pipe-delimited)
│   ├── feedback.txt
│   ├── instructors.txt
│   ├── lessons.txt
│   ├── payments.txt
│   ├── students.txt
│   └── vehicles.txt
├── src/
│   └── main/
│       ├── java/
│       │   └── lk/drivingschool/
│       │       ├── datastructure/       # Custom Data Structures & Algorithms
│       │       │   ├── BubbleSort.java  # Module 3: Instructor ranking
│       │       │   └── LessonQueue.java # Module 2: FIFO lesson scheduling
│       │       ├── model/               # OOP Domain Entities
│       │       │   ├── Feedback.java
│       │       │   ├── Instructor.java  # Extends Person
│       │       │   ├── Lesson.java
│       │       │   ├── Payment.java
│       │       │   ├── Person.java      # Base abstract/superclass
│       │       │   ├── Student.java     # Extends Person
│       │       │   ├── UserRole.java
│       │       │   └── Vehicle.java
│       │       ├── service/             # Data aggregation & business logic
│       │       │   └── DataService.java
│       │       ├── servlet/             # Jakarta EE HTTP Controllers
│       │       │   ├── AuthFilter.java  # Security gatekeeper
│       │       │   ├── AuthServlet.java
│       │       │   ├── FeedbackServlet.java
│       │       │   ├── HomeServlet.java # Dashboard controller
│       │       │   ├── InstructorServlet.java
│       │       │   ├── LessonServlet.java
│       │       │   ├── PaymentServlet.java
│       │       │   ├── StudentServlet.java
│       │       │   └── VehicleServlet.java
│       │       └── util/                # File I/O persistence engine
│       │           └── FileHandler.java
│       └── webapp/                      # JSP Presentation Layer
│           ├── css/
│           │   └── style.css            # Minimalist custom styling
│           ├── WEB-INF/
│           │   └── views/               # JSP Views & Layout Fragments
│           │       ├── dashboard.jsp
│           │       ├── feedback.jsp
│           │       ├── instructors.jsp
│           │       ├── layout_footer.jspf
│           │       ├── layout_header.jspf
│           │       ├── lessons.jsp
│           │       ├── login.jsp
│           │       ├── payments.jsp
│           │       ├── students.jsp
│           │       └── vehicles.jsp
│           └── index.jsp                # Welcome redirector
├── pom.xml                              # Maven project configuration (Jakarta EE 10)
├── build_war.bat                        # Automated Windows build script
├── run_standalone.bat                   # Standalone execution helper
└── README.md                            # Comprehensive project documentation
```

---

## ⚙️ 7. Prerequisites & Technology Stack

* **Java Development Kit (JDK):** Version 17 or higher
* **Build Tool:** Apache Maven 3.8+
* **Web Container:** Apache Tomcat 10.1+ (Supports `jakarta.servlet` namespace)
* **Web Technologies:** JSP 3.0, JSTL 3.0, Bootstrap 5.3, Bootstrap Icons 1.11
* **Version Control:** Git & GitHub

---

## 🚀 8. Build, Deploy & Run

### Method A: Maven Command Line
1. Open a terminal in the root folder of the project.
2. Build and package the project into a `.war` file:
   ```bash
   mvn clean package
   ```
3. The packaged WAR will be generated at:
   ```
   target/DrivingSchoolSystem.war
   ```
4. Copy `target/DrivingSchoolSystem.war` into the `webapps/` directory of your Apache Tomcat 10.1+ installation.
5. Start Tomcat (`bin/startup.bat` on Windows or `bin/startup.sh` on Linux/macOS).

### Method B: Standalone Scripts (Windows)
* Double-click `build_war.bat` to automatically invoke Maven and build the archive.
* Double-click `run_standalone.bat` for fast local testing.

### Method C: Accessing the Web Application
Once the server is running, navigate to:
```
http://localhost:8080/DrivingSchoolSystem/
```

---

## 🔐 9. Default User Accounts (Role-Based Access)

Use the following credentials on the login screen (`/login`) to explore different role perspectives:

| Role | Username | Password | Access Privileges |
|:---|:---:|:---:|:---|
| **System Administrator** | `admin` | `admin123` | Full access to all 6 modules, queue dispatching, payments, and student records |
| **Driving Instructor** | `instructor` | `ins123` | Access to student roster, lesson schedules, and assigned vehicles |
| **Enrolled Student** | `student` | `stu123` | View personal lessons, book new queue slots, view payments, and submit feedback |

---

## 🌿 10. Git Collaboration & Branching Strategy

Our team followed professional Git practices throughout development:
* **`master` / `main`:** Production-ready integrated codebase.
* **Feature Branches:** Each member developed independently on an assigned branch (`student-it26101058`, `lesson-IT26101056`, `feature/instructor-management`, `IT26101055`, `payment-billing`, `member-6`).
* **Pull Request Workflow:** Changes were reviewed, checked for merge conflicts, and integrated by the **Group Leader (`Manitha79`)**.

---

## 📜 11. License & Academic Integrity
This project was developed for academic evaluation purposes as part of the **SLIIT Object-Oriented Programming (OOP) Module**. All rights reserved by **SKU-GROUP 22 (2026)**.
