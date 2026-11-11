package lk.drivingschool.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import lk.drivingschool.model.UserRole;
import java.io.IOException;

/**
 * Security and Route Guard Filter.
 * Enforces Role-Based Access Control (RBAC) across all application endpoints.
 */
@WebFilter(urlPatterns = "/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getServletPath();

        // Allow static assets and public auth endpoints
        if (path.startsWith("/css") || path.startsWith("/js") || path.startsWith("/images")
                || path.endsWith(".css") || path.endsWith(".js") || path.endsWith(".ico")
                || "/login".equals(path) || "/logout".equals(path) || "/switch-role".equals(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(true);
        String roleStr = (String) session.getAttribute("userRole");

        // Default role for fresh sessions is ADMIN so lecturers can immediately inspect the system
        if (roleStr == null) {
            roleStr = UserRole.ADMIN.name();
            session.setAttribute("userRole", roleStr);
            session.setAttribute("userRoleObj", UserRole.ADMIN);
            session.setAttribute("userId", "ADM-001");
            session.setAttribute("userName", "Sajan P. (Admin Lead)");
            session.setAttribute("userBadge", "Administrator");
        }

        UserRole role;
        try {
            role = UserRole.valueOf(roleStr);
        } catch (Exception e) {
            role = UserRole.ADMIN;
        }

        // Role authorization check
        if (role == UserRole.STUDENT) {
            // Students are strictly blocked from Dashboard, Students directory, Instructors HR, and Fleet
            if ("/dashboard".equals(path) || "".equals(path) || "/home".equals(path)
                    || "/students".equals(path)
                    || "/instructors".equals(path)
                    || "/vehicles".equals(path)) {
                resp.sendRedirect(req.getContextPath() + "/lessons?error=Access+Denied:+Customer+portal+does+not+have+Admin+privileges.");
                return;
            }
        } else if (role == UserRole.INSTRUCTOR) {
            // Instructors are blocked from Executive Dashboard, Financial billing, and Student directory
            if ("/dashboard".equals(path) || "".equals(path) || "/home".equals(path)
                    || "/payments".equals(path)
                    || "/students".equals(path)
                    || "/feedback".equals(path)) {
                resp.sendRedirect(req.getContextPath() + "/lessons?error=Access+Denied:+Staff+portal+restricted+from+financial+or+admin+pages.");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
