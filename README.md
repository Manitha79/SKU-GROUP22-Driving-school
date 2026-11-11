# 💳 Payment & Billing Management Module
> **Apex Driving School System – SLIIT Object-Oriented Programming (OOP) Coursework**  
> **Academic Year:** 2026 | **Group ID:** SKU-GROUP 22  
> **Student Name:** Weerakkody M.R.B (Manitha)  
> **Student ID:** `IT26101059`  
> **Project Role:** Group Leader & Payment/Billing Component Lead  
> **Feature Branch:** `payment-billing`  
> **Pull Request Target:** `master`  

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Jakarta EE](https://img.shields.io/badge/Jakarta_Servlet-6.0-F80000?style=for-the-badge&logo=jakartaee&logoColor=white)](https://jakarta.ee/)
[![JSP](https://img.shields.io/badge/JSP-3.0-007396?style=for-the-badge&logo=java&logoColor=white)](https://jakarta.ee/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)](https://getbootstrap.com/)
[![Git](https://img.shields.io/badge/Branch-payment--billing-F05032?style=for-the-badge&logo=git&logoColor=white)](https://github.com/Manitha79/SKU-GROUP22-Driving-school/tree/payment-billing)

---

## 📌 1. Module Overview

The **Payment & Billing Management Module** is the core financial engine of the Apex Driving School web application. It is solely designed, implemented, and maintained by **Weerakkody M.R.B (`IT26101059`)** on the `payment-billing` feature branch.

This component automates:
* **Student Tuition & Invoicing:** Generates unique invoices for driving packages and individual lesson fees.
* **Multi-Method Payment Processing:** Supports `CASH`, `CARD`, and `ONLINE_TRANSFER` transactions.
* **Financial Aggregation & KPIs:** Dynamically calculates total academy revenue, pending student dues, and transaction volumes for the executive dashboard.
* **File-Based Persistence:** Serializes and deserializes payment ledger records in `data/payments.txt` via thread-safe I/O operations without requiring an external SQL database.
* **Status Lifecycle Management:** Tracks each financial ledger entry through its lifecycle (`PAID`, `PENDING`, `REFUNDED`).

---

## 📂 2. Files Contributed & Architecture

This feature branch strictly adheres to the **Model-View-Controller (MVC)** architectural design:

```
src/main/
├── java/lk/drivingschool/
│   ├── model/
│   │   └── Payment.java              # [MODEL] Encapsulated domain entity
│   ├── servlet/
│   │   └── PaymentServlet.java       # [CONTROLLER] HTTP request routing & CRUD
│   └── service/
│       └── DataService.java          # [SERVICE] Payment aggregation & business logic
└── webapp/WEB-INF/views/
    └── payments.jsp                  # [VIEW] Responsive billing UI & revenue analytics
data/
└── payments.txt                      # [DATABASE] Persistent pipe-delimited ledger
```

### Detailed File Responsibilities:
| File | Layer | Primary Responsibility |
|:---|:---:|:---|
| **`Payment.java`** | Model | Encapsulates payment properties, defensive mutators, and serialization formatting. |
| **`PaymentServlet.java`** | Controller | Intercepts `GET` (view ledger, filter by status) and `POST` (create invoice, update payment status). |
| **`payments.jsp`** | View | Renders summary metric cards, registration modal, clean transaction tables, and status badges. |
| **`payments.txt`** | Persistence | Flat-file store format: `PAY-ID\|STUDENT-ID\|AMOUNT\|DATE\|METHOD\|STATUS`. |

---

## 💡 3. OOP Concepts Implemented in this Module

### A. Encapsulation (Data Hiding & Validation)
To maintain financial data integrity, all properties in `Payment.java` are strictly declared as `private`. Direct access is forbidden; state alterations must pass through validated accessor and mutator methods:

```java
package lk.drivingschool.model;

public class Payment {
    private String id;
    private String studentId;
    private double amount;
    private String paymentDate;
    private String method; // CASH, CARD, ONLINE_TRANSFER
    private String status; // PAID, PENDING, REFUNDED

    public Payment(String id, String studentId, double amount, 
                   String paymentDate, String method, String status) {
        this.id = id;
        this.studentId = studentId;
        setAmount(amount); // Enforces defensive boundary checking
        this.paymentDate = paymentDate;
        this.method = method;
        this.status = status;
    }

    // Defensive validation: Prevents zero or negative payment entries
    public void setAmount(double amount) {
        if (amount > 0) {
            this.amount = amount;
        } else {
            throw new IllegalArgumentException("Payment amount must be greater than zero.");
        }
    }

    public double getAmount() {
        return this.amount;
    }

    public String getId() { return id; }
    public String getStudentId() { return studentId; }
    public String getPaymentDate() { return paymentDate; }
    public String getMethod() { return method; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
```

### B. Abstraction (Layered Separation of Concerns)
* The `PaymentServlet` does not directly execute low-level file write operations.
* File I/O mechanics (splitting pipes, reading lines, stream closing) are abstracted inside `FileHandler` and `DataService`.
* The controller simply invokes high-level domain methods:
  ```java
  DataService.savePayment(newPayment);
  List<Payment> payments = DataService.getAllPayments();
  double totalRevenue = DataService.calculateTotalRevenue();
  ```

### C. State Pattern / Status Machine
Payments adhere to strict operational states:
* `PAID`: Transaction successfully processed and counted toward Total Revenue.
* `PENDING`: Invoice issued to student, awaiting payment clearance.
* `REFUNDED`: Cancelled lesson or course package with refunded funds.

---

## 📊 4. Financial Calculations & Business Logic

The module provides real-time mathematical aggregation across all records in `payments.txt`:

$$\text{Total Collected Revenue} = \sum_{i \in \text{PAID}} \text{Amount}_i$$

$$\text{Outstanding Receivables} = \sum_{j \in \text{PENDING}} \text{Amount}_j$$

These figures are computed on every `doGet` request and forwarded to `payments.jsp` and the executive `dashboard.jsp`.

---

## 🧪 5. Step-by-Step Viva Demonstration Script

During your viva examination, follow this exact sequence to demonstrate your module to the evaluator:

1. **Access the Module:**  
   Open your browser and navigate to:  
   👉 `http://localhost:8080/DrivingSchoolSystem/payments`

2. **Demonstrate Dashboard KPI Cards:**  
   Point out the top analytics row:
   * **Total Revenue:** Show the formatted currency value (e.g., `LKR 115,000`).
   * **Paid Transactions Count:** Point out the count of cleared payments.
   * **Pending Payments Count:** Show overdue/pending student fees.

3. **Demonstrate Adding a New Payment (CRUD - Create):**  
   Click the **"Record New Payment"** button:
   * **Student ID:** Select or enter `STU-1002` (Nimal Silva).
   * **Amount:** Enter `25000.00`.
   * **Payment Date:** Select current date.
   * **Payment Method:** Choose `CARD`.
   * **Status:** Select `PAID`.
   * Click **"Save Payment"**.

4. **Verify Real-Time Updates & Persistence:**  
   * Show that the new payment immediately appears at the top of the table.
   * Highlight that the **Total Revenue** KPI automatically incremented by `LKR 25,000`.
   * Open `data/payments.txt` on your computer to prove to the lecturer that the record was permanently persisted to the file database.

5. **Demonstrate Status Update / Voiding:**  
   Toggle a payment status from `PENDING` to `PAID` or view the simulated invoice receipt.

---

## 🌿 6. Pull Request & Branch Details

* **Author:** Weerakkody M.R.B (`IT26101059`)
* **Email:** `IT26101059@my.sliit.lk`
* **Feature Branch:** `payment-billing`
* **Target Integration Branch:** `master`
* **Commits:** 15+ descriptive atomic commits documenting iterative feature development.
