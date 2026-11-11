package lk.drivingschool.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lk.drivingschool.service.DataService;
import java.io.IOException;

/**
 * Controller for the Executive Home Dashboard.
 * Member 6: Sajan P. (Admin Overview)
 */
@WebServlet(urlPatterns = {"", "/dashboard", "/home"})
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("stats", DataService.getDashboardStats());
        req.setAttribute("recentLessons", DataService.getLessons());
        req.setAttribute("instructors", DataService.getInstructorsSorted(false)); // Bubble sorted!
        req.setAttribute("vehicles", DataService.getVehicles());
        req.setAttribute("feedbacks", DataService.getFeedbacks());

        req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
    }
}
