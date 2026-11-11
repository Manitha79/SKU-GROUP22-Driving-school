package lk.drivingschool.model;

/**
 * Payment Model class.
 * Member 5: Weerakkody M.R.B
 * OOP Concepts:
 * 1. Encapsulation: Private financial attributes with getters and setters.
 * 2. Revenue Aggregation: DataService calculates total revenue of paid invoices.
 */
public class Payment {
    private String id;
    private String studentId;
    private double amount;
    private String paymentDate;
    private String method; // "CASH", "CARD", "BANK_TRANSFER", "ONLINE"
    private String status; // "PAID", "PENDING"

    public Payment(String id, String studentId, double amount, String paymentDate, String method, String status) {
        this.id = id;
        this.studentId = studentId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.method = method;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaymentDate() { return paymentDate; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
