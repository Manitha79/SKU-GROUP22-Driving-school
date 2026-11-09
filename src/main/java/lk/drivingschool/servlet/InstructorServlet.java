package lk.drivingschool.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lk.drivingschool.model.Instructor;
import lk.drivingschool.service.DataService;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Controller for Instructor Management & Bubble Sort.
 * Member 3: Kumarawansha W.K.R.D
 * Performs CRUD operations and invokes BubbleSort by experience.
 */
@WebServlet(urlPatterns = {"/instructor", "/instructors"})
public class InstructorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String id = req.getParameter("id");
        String q = req.getParameter("q");
        String sort = req.getParameter("sort");

        boolean ascending = "asc".equalsIgnoreCase(sort);
        List<Instructor> instructors = DataService.getInstructorsSorted(ascending);

        if ("edit".equalsIgnoreCase(action) && id != null) {
            for (Instructor ins : instructors) {
                if (ins.getId().equalsIgnoreCase(id)) {
                    req.setAttribute("editItem", ins);
                    break;
                }
            }
        }

        if (q != null && !q.isBlank()) {
            String query = q.toLowerCase();
            instructors.removeIf(ins -> !ins.getName().toLowerCase().contains(query) &&
                                        !ins.getLicenseCategory().toLowerCase().contains(query) &&
                                        !ins.getId().toLowerCase().contains(query));
        }

        req.setAttribute("instructors", instructors);
        req.setAttribute("sortOrder", ascending ? "asc" : "desc");
        req.getRequestDispatcher("/WEB-INF/views/instructors.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String id = req.getParameter("id");

        try {
            if ("delete".equalsIgnoreCase(action)) {
                DataService.deleteInstructor(id);
            } else if ("create".equalsIgnoreCase(action) || "update".equalsIgnoreCase(action)) {
                boolean isNew = "create".equalsIgnoreCase(action);
                if (isNew) {
                    id = "INS-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
                }

                String name = req.getParameter("name");
                String email = req.getParameter("email");
                String phone = req.getParameter("phone");
                String licenseCategory = req.getParameter("licenseCategory");
                if (licenseCategory == null || licenseCategory.isBlank()) {
                    licenseCategory = req.getParameter("licenceCategory");
                }
                int experienceYears = 0;
                try {
                    experienceYears = Integer.parseInt(req.getParameter("experienceYears"));
                } catch (Exception ignored) {}
                String status = req.getParameter("status");
                if (status == null || status.isBlank()) status = "ACTIVE";

                Instructor ins = new Instructor(id, name, email, phone, licenseCategory, experienceYears, status);
                if (isNew) {
                    DataService.addInstructor(ins);
                } else {
                    DataService.updateInstructor(ins);
                }
            }
            resp.sendRedirect(req.getContextPath() + "/instructors?success=true");
        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("instructors", DataService.getInstructors());
            req.getRequestDispatcher("/WEB-INF/views/instructors.jsp").forward(req, resp);
        }
    }
}
