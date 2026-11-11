package lk.drivingschool.model;

/**
 * User roles for Role-Based Access Control (RBAC).
 * Distinguishes between School Administrators, Customers/Students, and Instructors.
 */
public enum UserRole {
    ADMIN("Administrator", "Full system access to all modules, financial reporting, and CRUD."),
    STUDENT("Student / Customer", "Customer portal to book lessons, view receipts, and submit feedback."),
    INSTRUCTOR("Instructor / Staff", "Staff portal to view assigned training lessons, fleet vehicles, and experience ranking.");

    private final String displayName;
    private final String description;

    UserRole(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean canAccessDashboard() {
        return this == ADMIN;
    }

    public boolean canAccessStudents() {
        return this == ADMIN;
    }

    public boolean canAccessInstructors() {
        return this == ADMIN || this == INSTRUCTOR;
    }

    public boolean canAccessVehicles() {
        return this == ADMIN || this == INSTRUCTOR;
    }

    public boolean canAccessPayments() {
        return this == ADMIN || this == STUDENT;
    }

    public boolean canAccessFeedback() {
        return this == ADMIN || this == STUDENT;
    }

    public boolean canAccessLessons() {
        return true; // All roles can access lessons (contextual view)
    }

    public boolean canDeleteRecords() {
        return this == ADMIN;
    }
}
