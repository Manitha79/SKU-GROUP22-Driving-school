package lk.drivingschool.service;

import lk.drivingschool.datastructure.BubbleSort;
import lk.drivingschool.datastructure.LessonQueue;
import lk.drivingschool.model.*;
import lk.drivingschool.util.FileHandler;

import java.util.*;

/**
 * Main Business Service coordinating the 6 modules with the FileHandler.
 * Converts raw text file rows into clean OOP model objects.
 */
public class DataService {

    // ==============================================================
    // 1. STUDENT MANAGEMENT (Member 1: Dinushan R.K)
    // ==============================================================
    public static List<Student> getStudents() {
        List<Student> list = new ArrayList<>();
        for (String[] r : FileHandler.load("students.txt")) {
            if (r.length >= 6) {
                list.add(new Student(r[0], r[1], r[2], r[3], r[4], r[5]));
            }
        }
        return list;
    }

    public static void addStudent(Student s) {
        String[] row = new String[]{s.getId(), s.getName(), s.getEmail(), s.getPhone(), s.getLicenseType(), s.getAddress()};
        FileHandler.append("students.txt", row);
    }

    public static void updateStudent(Student s) {
        String[] row = new String[]{s.getId(), s.getName(), s.getEmail(), s.getPhone(), s.getLicenseType(), s.getAddress()};
        FileHandler.update("students.txt", s.getId(), row);
    }

    public static void deleteStudent(String id) {
        FileHandler.delete("students.txt", id);
    }

    public static Student findStudentById(String id) {
        for (Student s : getStudents()) {
            if (s.getId().equalsIgnoreCase(id)) return s;
        }
        return null;
    }

    // ==============================================================
    // 2. LESSON SCHEDULING & QUEUE (Member 2: Thanneliyadda T.L.K.S)
    // ==============================================================
    public static List<Lesson> getLessons() {
        List<Lesson> list = new ArrayList<>();
        for (String[] r : FileHandler.load("lessons.txt")) {
            if (r.length >= 7) {
                list.add(new Lesson(r[0], r[1], r[2], r[3], r[4], r[5], r[6]));
            }
        }
        return list;
    }

    public static LessonQueue getLessonQueue() {
        LessonQueue queue = new LessonQueue();
        for (Lesson l : getLessons()) {
            if ("PENDING_QUEUE".equalsIgnoreCase(l.getStatus())) {
                queue.enqueue(l);
            }
        }
        return queue;
    }

    public static Lesson processNextInQueue() {
        LessonQueue queue = getLessonQueue();
        Lesson next = queue.dequeue();
        if (next != null) {
            next.setStatus("CONFIRMED");
            updateLesson(next);
        }
        return next;
    }

    public static void addLesson(Lesson l) {
        String[] row = new String[]{l.getId(), l.getStudentId(), l.getInstructorId(), l.getLessonDate(), l.getLessonTime(), l.getNotes(), l.getStatus()};
        FileHandler.append("lessons.txt", row);
    }

    public static void updateLesson(Lesson l) {
        String[] row = new String[]{l.getId(), l.getStudentId(), l.getInstructorId(), l.getLessonDate(), l.getLessonTime(), l.getNotes(), l.getStatus()};
        FileHandler.update("lessons.txt", l.getId(), row);
    }

    public static void deleteLesson(String id) {
        FileHandler.delete("lessons.txt", id);
    }

    // ==============================================================
    // 3. INSTRUCTOR MANAGEMENT & BUBBLE SORT (Member 3: Kumarawansha)
    // ==============================================================
    public static List<Instructor> getInstructors() {
        List<Instructor> list = new ArrayList<>();
        for (String[] r : FileHandler.load("instructors.txt")) {
            if (r.length >= 7) {
                int exp = 0;
                try { exp = Integer.parseInt(r[5]); } catch (Exception ignored) {}
                list.add(new Instructor(r[0], r[1], r[2], r[3], r[4], exp, r[6]));
            }
        }
        return list;
    }

    public static List<Instructor> getInstructorsSorted(boolean ascending) {
        List<Instructor> list = getInstructors();
        if (ascending) {
            BubbleSort.sortByExperienceAscending(list);
        } else {
            BubbleSort.sortByExperienceDescending(list);
        }
        return list;
    }

    public static void addInstructor(Instructor ins) {
        String[] row = new String[]{ins.getId(), ins.getName(), ins.getEmail(), ins.getPhone(), ins.getLicenseCategory(), String.valueOf(ins.getExperienceYears()), ins.getStatus()};
        FileHandler.append("instructors.txt", row);
    }

    public static void updateInstructor(Instructor ins) {
        String[] row = new String[]{ins.getId(), ins.getName(), ins.getEmail(), ins.getPhone(), ins.getLicenseCategory(), String.valueOf(ins.getExperienceYears()), ins.getStatus()};
        FileHandler.update("instructors.txt", ins.getId(), row);
    }

    public static void deleteInstructor(String id) {
        FileHandler.delete("instructors.txt", id);
    }

    // ==============================================================
    // 4. VEHICLE FLEET MANAGEMENT (Member 4: Senevirathna M.G.T.R)
    // ==============================================================
    public static List<Vehicle> getVehicles() {
        List<Vehicle> list = new ArrayList<>();
        for (String[] r : FileHandler.load("vehicles.txt")) {
            if (r.length >= 6) {
                list.add(new Vehicle(r[0], r[1], r[2], r[3], r[4], r[5]));
            }
        }
        return list;
    }

    public static void addVehicle(Vehicle v) {
        String[] row = new String[]{v.getId(), v.getPlateNumber(), v.getModel(), v.getCategory(), v.getTransmission(), v.getStatus()};
        FileHandler.append("vehicles.txt", row);
    }

    public static void updateVehicle(Vehicle v) {
        String[] row = new String[]{v.getId(), v.getPlateNumber(), v.getModel(), v.getCategory(), v.getTransmission(), v.getStatus()};
        FileHandler.update("vehicles.txt", v.getId(), row);
    }

    public static void deleteVehicle(String id) {
        FileHandler.delete("vehicles.txt", id);
    }

    // ==============================================================
    // 5. PAYMENT & BILLING (Member 5: Weerakkody M.R.B)
    // ==============================================================
    public static List<Payment> getPayments() {
        List<Payment> list = new ArrayList<>();
        for (String[] r : FileHandler.load("payments.txt")) {
            if (r.length >= 6) {
                double amount = 0.0;
                try { amount = Double.parseDouble(r[2]); } catch (Exception ignored) {}
                list.add(new Payment(r[0], r[1], amount, r[3], r[4], r[5]));
            }
        }
        return list;
    }

    public static double getTotalRevenue() {
        double total = 0.0;
        for (Payment p : getPayments()) {
            if ("PAID".equalsIgnoreCase(p.getStatus())) {
                total += p.getAmount();
            }
        }
        return total;
    }

    public static void addPayment(Payment p) {
        String[] row = new String[]{p.getId(), p.getStudentId(), String.format(Locale.US, "%.2f", p.getAmount()), p.getPaymentDate(), p.getMethod(), p.getStatus()};
        FileHandler.append("payments.txt", row);
    }

    public static void updatePayment(Payment p) {
        String[] row = new String[]{p.getId(), p.getStudentId(), String.format(Locale.US, "%.2f", p.getAmount()), p.getPaymentDate(), p.getMethod(), p.getStatus()};
        FileHandler.update("payments.txt", p.getId(), row);
    }

    public static void deletePayment(String id) {
        FileHandler.delete("payments.txt", id);
    }

    // ==============================================================
    // 6. ADMIN & FEEDBACK (Member 6: Sajan P.)
    // ==============================================================
    public static List<Feedback> getFeedbacks() {
        List<Feedback> list = new ArrayList<>();
        for (String[] r : FileHandler.load("feedback.txt")) {
            if (r.length >= 5) {
                int rating = 5;
                try { rating = Integer.parseInt(r[2]); } catch (Exception ignored) {}
                list.add(new Feedback(r[0], r[1], rating, r[3], r[4]));
            }
        }
        return list;
    }

    public static double getAverageRating() {
        List<Feedback> list = getFeedbacks();
        if (list.isEmpty()) return 5.0;
        double sum = 0.0;
        for (Feedback f : list) {
            sum += f.getRating();
        }
        return sum / list.size();
    }

    public static void addFeedback(Feedback f) {
        String[] row = new String[]{f.getId(), f.getStudentId(), String.valueOf(f.getRating()), f.getComments(), f.getStatus()};
        FileHandler.append("feedback.txt", row);
    }

    public static void updateFeedback(Feedback f) {
        String[] row = new String[]{f.getId(), f.getStudentId(), String.valueOf(f.getRating()), f.getComments(), f.getStatus()};
        FileHandler.update("feedback.txt", f.getId(), row);
    }

    public static void deleteFeedback(String id) {
        FileHandler.delete("feedback.txt", id);
    }

    // ==============================================================
    // DASHBOARD OVERVIEW: Aggregates metrics from all 6 members
    // ==============================================================
    public static Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", getStudents().size());
        stats.put("queuedLessons", getLessonQueue().size());
        stats.put("totalInstructors", getInstructors().size());
        stats.put("totalVehicles", getVehicles().size());
        stats.put("totalRevenue", getTotalRevenue());
        stats.put("avgRating", String.format(Locale.US, "%.1f", getAverageRating()));
        stats.put("feedbackCount", getFeedbacks().size());
        return stats;
    }
}
