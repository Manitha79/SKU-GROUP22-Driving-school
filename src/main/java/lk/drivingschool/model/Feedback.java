package lk.drivingschool.model;

/**
 * Feedback Model class.
 * Member 6: Sajan P. (Admin & Feedback)
 * OOP Concepts:
 * 1. Encapsulation: Private feedback attributes with getters and setters.
 * 2. Analytics: Used to compute customer satisfaction score.
 */
public class Feedback {
    private String id;
    private String studentId;
    private int rating; // 1 to 5 stars
    private String comments;
    private String status; // "NEW", "REVIEWED"

    public Feedback(String id, String studentId, int rating, String comments, String status) {
        this.id = id;
        this.studentId = studentId;
        this.rating = rating;
        this.comments = comments;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public String getComment() { return comments; }
    public void setComment(String comment) { this.comments = comment; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Helper method to display star characters
    public String getRatingStars() {
        return "★".repeat(Math.max(0, Math.min(5, rating))) + "☆".repeat(Math.max(0, 5 - Math.min(5, rating)));
    }
}
