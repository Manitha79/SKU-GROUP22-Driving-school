package lk.drivingschool.service;

import lk.drivingschool.model.Feedback;
import lk.drivingschool.util.FileHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * Business Service for Member 6: Customer Feedback Module
 * Developer: Sajan P. (IT26101060)
 *
 * Responsibilities:
 * 1. CRUD operations for Customer Feedback (add, view, update, delete).
 * 2. Calculate average customer satisfaction score (1 to 5 stars).
 * 3. Coordinates with FileHandler for persistent storage in 'data/feedback.txt'.
 */
public class DataService {

    // 1. READ: Load all feedback records from data/feedback.txt
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

    // 2. ANALYTICS: Calculate the average rating score (e.g. 4.8 / 5.0)
    public static double getAverageRating() {
        List<Feedback> list = getFeedbacks();
        if (list.isEmpty()) return 5.0;
        double sum = 0.0;
        for (Feedback f : list) {
            sum += f.getRating();
        }
        return sum / list.size();
    }

    // 3. CREATE: Append a new feedback record to storage
    public static void addFeedback(Feedback f) {
        String[] row = new String[]{f.getId(), f.getStudentId(), String.valueOf(f.getRating()), f.getComments(), f.getStatus()};
        FileHandler.append("feedback.txt", row);
    }

    // 4. UPDATE: Modify an existing feedback record
    public static void updateFeedback(Feedback f) {
        String[] row = new String[]{f.getId(), f.getStudentId(), String.valueOf(f.getRating()), f.getComments(), f.getStatus()};
        FileHandler.update("feedback.txt", f.getId(), row);
    }

    // 5. DELETE: Remove a feedback record by ID
    public static void deleteFeedback(String id) {
        FileHandler.delete("feedback.txt", id);
    }
}
