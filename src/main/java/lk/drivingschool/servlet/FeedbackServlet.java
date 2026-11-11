package lk.drivingschool.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lk.drivingschool.model.Feedback;
import lk.drivingschool.service.DataService;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Controller for Admin & Feedback Management.
 * Member 6: Sajan P. (Admin & Feedback)
 * Performs CRUD operations and tracks customer satisfaction.
 */
@WebServlet(urlPatterns = {"/feedback", "/feedbacks", "/admin"})
public class FeedbackServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String id = req.getParameter("id");
        String q = req.getParameter("q");

        List<Feedback> feedbacks = DataService.getFeedbacks();

        if ("edit".equalsIgnoreCase(action) && id != null) {
            for (Feedback f : feedbacks) {
                if (f.getId().equalsIgnoreCase(id)) {
                    req.setAttribute("editItem", f);
                    break;
                }
            }
        }

        if (q != null && !q.isBlank()) {
            String query = q.toLowerCase();
            feedbacks.removeIf(f -> !f.getId().toLowerCase().contains(query) &&
                                    !f.getStudentId().toLowerCase().contains(query) &&
                                    !f.getComments().toLowerCase().contains(query));
        }

        req.setAttribute("feedbacks", feedbacks);
        req.setAttribute("avgRating", DataService.getAverageRating());
        req.setAttribute("students", DataService.getStudents());
        req.getRequestDispatcher("/WEB-INF/views/feedback.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String id = req.getParameter("id");

        try {
            if ("delete".equalsIgnoreCase(action)) {
                DataService.deleteFeedback(id);
            } else if ("create".equalsIgnoreCase(action) || "update".equalsIgnoreCase(action)) {
                boolean isNew = "create".equalsIgnoreCase(action);
                if (isNew) {
                    id = "FDB-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
                }

                String studentId = req.getParameter("studentId");
                int rating = 5;
                try {
                    rating = Integer.parseInt(req.getParameter("rating"));
                } catch (Exception ignored) {}

                String comments = req.getParameter("comments");
                String status = req.getParameter("status");
                if (status == null || status.isBlank()) status = "NEW";

                Feedback f = new Feedback(id, studentId, rating, comments, status);
                if (isNew) {
                    DataService.addFeedback(f);
                } else {
                    DataService.updateFeedback(f);
                }
            }
            resp.sendRedirect(req.getContextPath() + "/feedback?success=true");
        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("feedbacks", DataService.getFeedbacks());
            req.setAttribute("avgRating", DataService.getAverageRating());
            req.setAttribute("students", DataService.getStudents());
            req.getRequestDispatcher("/WEB-INF/views/feedback.jsp").forward(req, resp);
        }
    }
}
