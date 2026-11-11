package lk.drivingschool.model;

/**
 * Lesson Booking Model class.
 * Member 2: Thanneliyadda T.L.K.S
 * OOP Concepts:
 * 1. Encapsulation: Private booking attributes with getters and setters.
 * 2. FIFO Queue Ordering: Queued in LessonQueue when status is "PENDING_QUEUE".
 */
public class Lesson {
    private String id;
    private String studentId;
    private String instructorId;
    private String lessonDate;
    private String lessonTime;
    private String notes;
    private String status; // "PENDING_QUEUE", "CONFIRMED", "COMPLETED", "CANCELLED"

    public Lesson(String id, String studentId, String instructorId, String lessonDate, String lessonTime, String notes, String status) {
        this.id = id;
        this.studentId = studentId;
        this.instructorId = instructorId;
        this.lessonDate = lessonDate;
        this.lessonTime = lessonTime;
        this.notes = notes;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getInstructorId() { return instructorId; }
    public void setInstructorId(String instructorId) { this.instructorId = instructorId; }

    public String getLessonDate() { return lessonDate; }
    public void setLessonDate(String lessonDate) { this.lessonDate = lessonDate; }

    public String getLessonTime() { return lessonTime; }
    public void setLessonTime(String lessonTime) { this.lessonTime = lessonTime; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
