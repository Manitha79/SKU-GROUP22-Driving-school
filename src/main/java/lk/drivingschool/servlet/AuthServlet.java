package lk.drivingschool.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lk.drivingschool.model.UserRole;
import java.io.IOException;

/**
 * Controller for Authentication, Role-Switching, and Session Management.
 * Implements clean, student-friendly session authentication using standard HttpSession.
 */
@WebServlet(urlPatterns = {"/login", "/logout", "/switch-role"})
public class AuthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/logout".equals(path)) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/login?msg=Logged+out+successfully");
            return;
        }

        if ("/switch-role".equals(path)) {
            String roleParam = req.getParameter("role");
            String redirectParam = req.getParameter("redirect");
            if (roleParam != null) {
                applyRole(req.getSession(true), roleParam);
            }
            if (redirectParam != null && !redirectParam.isBlank()) {
                resp.sendRedirect(req.getContextPath() + redirectParam);
            } else {
                resp.sendRedirect(req.getContextPath() + getDefaultLandingPage(roleParam));
            }
            return;
        }

        // Render login page
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/switch-role".equals(path) || "/login".equals(path)) {
            String roleParam = req.getParameter("role");
            if (roleParam == null || roleParam.isBlank()) {
                roleParam = "ADMIN";
            }
            applyRole(req.getSession(true), roleParam);

            String redirectParam = req.getParameter("redirect");
            if (redirectParam != null && !redirectParam.isBlank()) {
                resp.sendRedirect(req.getContextPath() + redirectParam);
            } else {
                resp.sendRedirect(req.getContextPath() + getDefaultLandingPage(roleParam));
            }
        }
    }

    private void applyRole(HttpSession session, String roleStr) {
        try {
            UserRole role = UserRole.valueOf(roleStr.toUpperCase());
            session.setAttribute("userRole", role.name());
            session.setAttribute("userRoleObj", role);

            switch (role) {
                case ADMIN -> {
                    session.setAttribute("userId", "ADM-001");
                    session.setAttribute("userName", "Sajan P. (Admin Lead)");
                    session.setAttribute("userBadge", "Administrator");
                }
                case STUDENT -> {
                    session.setAttribute("userId", "STU-1001");
                    session.setAttribute("userName", "Kamal Perera (Student)");
                    session.setAttribute("userBadge", "Student / Customer");
                }
                case INSTRUCTOR -> {
                    session.setAttribute("userId", "INS-2001");
                    session.setAttribute("userName", "Sunil Bandara (Instructor)");
                    session.setAttribute("userBadge", "Instructor Staff");
                }
            }
        } catch (IllegalArgumentException e) {
            session.setAttribute("userRole", UserRole.ADMIN.name());
            session.setAttribute("userRoleObj", UserRole.ADMIN);
            session.setAttribute("userName", "Sajan P. (Admin Lead)");
        }
    }

    private String getDefaultLandingPage(String roleStr) {
        if ("STUDENT".equalsIgnoreCase(roleStr)) {
            return "/lessons";
        } else if ("INSTRUCTOR".equalsIgnoreCase(roleStr)) {
            return "/lessons";
        }
        return "/dashboard";
    }
}
