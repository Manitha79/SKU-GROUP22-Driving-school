package lk.drivingschool.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lk.drivingschool.model.Payment;
import lk.drivingschool.service.DataService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Controller for Payment & Billing.
 * Member 5: Weerakkody M.R.B
 * Performs CRUD operations and tracks revenue.
 */
@WebServlet(urlPatterns = {"/payment", "/payments"})
public class PaymentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String id = req.getParameter("id");
        String q = req.getParameter("q");

        List<Payment> payments = DataService.getPayments();

        if ("edit".equalsIgnoreCase(action) && id != null) {
            for (Payment p : payments) {
                if (p.getId().equalsIgnoreCase(id)) {
                    req.setAttribute("editItem", p);
                    break;
                }
            }
        }

        if (q != null && !q.isBlank()) {
            String query = q.toLowerCase();
            payments.removeIf(p -> !p.getId().toLowerCase().contains(query) &&
                                   !p.getStudentId().toLowerCase().contains(query) &&
                                   !p.getMethod().toLowerCase().contains(query));
        }

        req.setAttribute("payments", payments);
        req.setAttribute("totalRevenue", DataService.getTotalRevenue());
        req.setAttribute("students", DataService.getStudents());
        req.getRequestDispatcher("/WEB-INF/views/payments.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String id = req.getParameter("id");

        try {
            if ("delete".equalsIgnoreCase(action)) {
                DataService.deletePayment(id);
            } else if ("create".equalsIgnoreCase(action) || "update".equalsIgnoreCase(action)) {
                boolean isNew = "create".equalsIgnoreCase(action);
                if (isNew) {
                    id = "PAY-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
                }

                String studentId = req.getParameter("studentId");
                double amount = 0.0;
                try {
                    amount = Double.parseDouble(req.getParameter("amount"));
                } catch (Exception ignored) {}

                String date = req.getParameter("paymentDate");
                if (date == null || date.isBlank()) date = LocalDate.now().toString();

                String method = req.getParameter("method");
                String status = req.getParameter("status");

                if (method == null || method.isBlank()) method = "CASH";
                if (status == null || status.isBlank()) status = "PAID";

                Payment p = new Payment(id, studentId, amount, date, method, status);
                if (isNew) {
                    DataService.addPayment(p);
                } else {
                    DataService.updatePayment(p);
                }
            }
            resp.sendRedirect(req.getContextPath() + "/payments?success=true");
        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("payments", DataService.getPayments());
            req.setAttribute("totalRevenue", DataService.getTotalRevenue());
            req.setAttribute("students", DataService.getStudents());
            req.getRequestDispatcher("/WEB-INF/views/payments.jsp").forward(req, resp);
        }
    }
}
