# 💳 Module 05: Payment & Billing Management System
> **Coursework:** SLIIT – Object-Oriented Programming (OOP)  
> **Student Name:** Weerakkody M.R.B (Manitha)  
> **Student ID:** `IT26101059`  
> **Allocated Role:** Group Leader & Payment/Billing Lead  
> **Git Feature Branch:** `payment-billing`  

---

## 📌 1. Module Overview
The **Payment and Billing Management Module** manages the financial accounts, course fee invoices, and revenue records for student driving packages. It enables creating invoices, processing payments through multiple methods (`CASH`, `CARD`, `ONLINE_TRANSFER`), computing cumulative revenues, and generating billing summaries.

---

## 📂 2. Key Files Contributed
| File Path | Component Type | Responsibility |
|:---|:---|:---|
| `src/main/java/lk/drivingschool/model/Payment.java` | Domain Entity | Encapsulates transaction ID, student reference, amount, payment method, status. |
| `src/main/java/lk/drivingschool/servlet/PaymentServlet.java` | Controller | Handles payment logging, revenue queries, status updates, and JSP routing. |
| `src/main/webapp/WEB-INF/views/payments.jsp` | View Layer (JSP) | Revenue KPI cards, payment receipt simulation, transaction history table. |
| `data/payments.txt` | File Database | Pipe-delimited persistence store for all financial transactions. |

---

## 💡 3. OOP Implementation & Financial Logic

### A. Encapsulated Model (`Payment.java`):
```java
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
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.method = method;
        this.status = status;
    }

    // Encapsulated getters & setters with defensive boundary checks
    public double getAmount() { return amount; }
    public void setAmount(double amount) {
        if (amount > 0) {
            this.amount = amount;
        }
    }
}
```

### B. Business & Aggregate Analytics (`DataService.java`):
The module aggregates financial data across all recorded payments:
* **Total Revenue:** Sum of all transactions marked with status `PAID`.
* **Pending Receivables:** Unpaid fee balances awaiting settlement.
* **Payment Method Distribution:** Analytics across Cash, Card, and Bank Transfers.

---

## 🧪 4. Testing & Viva Walkthrough
1. **Navigate to:** `http://localhost:8080/DrivingSchoolSystem/payments`
2. **Review Dashboard KPIs:**
   * Observe **Total Collected Revenue** (e.g. `LKR 115,000`).
   * Observe **Total Completed Transactions** count.
3. **Record a Payment:** Click **"Record New Payment"**:
   * **Student ID:** `STU-1002` (Nimal Silva)
   * **Amount (LKR):** `25000.00`
   * **Payment Date:** `2026-11-12`
   * **Payment Method:** `CARD`
   * **Status:** `PAID`
4. Submit -> Verify that the table updates and total revenue increments in real-time.

---

## 🎯 5. Expected Viva Questions & Answers
* **Q1: How do you prevent invalid or negative payment amounts in OOP?**  
  * *Answer:* Through encapsulation in `Payment.java`. The `setAmount(double)` method and constructor include validation conditions preventing zero or negative values.
* **Q2: How does the file storage guarantee record separation?**  
  * *Answer:* Records are serialized into pipe-delimited strings (`PAY-5001|STU-1001|35000.0|2026-11-01|CARD|PAID`) in `data/payments.txt` via thread-safe file writing routines.
