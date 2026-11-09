package lk.drivingschool.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lk.drivingschool.model.Student;
import lk.drivingschool.service.DataService;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Controller for Student Management.
 * Member 1: Dinushan R.K
 * Performs CRUD operations on Student records.
 */
@WebServlet(urlPatterns = {"/student", "/students"})
public class StudentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String id = req.getParameter("id");
        String q = req.getParameter("q");

        List<Student> students = DataService.getStudents();

        // 1. Edit Action: find student and attach to request
        if ("edit".equalsIgnoreCase(action) && id != null) {
            req.setAttribute("editItem", DataService.findStudentById(id));
        }

        // 2. Search Action: filter students by name, email, or ID
        if (q != null && !q.isBlank()) {
            String query = q.toLowerCase();
            students.removeIf(s -> !s.getName().toLowerCase().contains(query) &&
                                   !s.getEmail().toLowerCase().contains(query) &&
                                   !s.getId().toLowerCase().contains(query));
        }

        req.setAttribute("students", students);
        req.getRequestDispatcher("/WEB-INF/views/students.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String id = req.getParameter("id");

        try {
            if ("delete".equalsIgnoreCase(action)) {
                // DELETE: remove student
                DataService.deleteStudent(id);
            } else if ("create".equalsIgnoreCase(action) || "update".equalsIgnoreCase(action)) {
                // CREATE or UPDATE student
                boolean isNew = "create".equalsIgnoreCase(action);
                if (isNew) {
                    id = "STU-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
                }

                String name = req.getParameter("name");
                String email = req.getParameter("email");
                String phone = req.getParameter("phone");
                String licenseType = req.getParameter("licenseType");
                if (licenseType == null || licenseType.isBlank()) {
                    licenseType = req.getParameter("licenceType");
                }
                String address = req.getParameter("address");

                Student s = new Student(id, name, email, phone, licenseType, address);
                if (isNew) {
                    DataService.addStudent(s);
                } else {
                    DataService.updateStudent(s);
                }
            }
            resp.sendRedirect(req.getContextPath() + "/students?success=true");
        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("students", DataService.getStudents());
            req.getRequestDispatcher("/WEB-INF/views/students.jsp").forward(req, resp);
        }
    }
}
