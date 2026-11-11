package lk.drivingschool.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lk.drivingschool.model.Vehicle;
import lk.drivingschool.service.DataService;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Controller for Vehicle Fleet Management.
 * Member 4: Senevirathna M.G.T.R
 * Performs CRUD operations on training fleet.
 */
@WebServlet(urlPatterns = {"/vehicle", "/vehicles"})
public class VehicleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String id = req.getParameter("id");
        String q = req.getParameter("q");

        List<Vehicle> vehicles = DataService.getVehicles();

        if ("edit".equalsIgnoreCase(action) && id != null) {
            for (Vehicle v : vehicles) {
                if (v.getId().equalsIgnoreCase(id)) {
                    req.setAttribute("editItem", v);
                    break;
                }
            }
        }

        if (q != null && !q.isBlank()) {
            String query = q.toLowerCase();
            vehicles.removeIf(v -> !v.getPlateNumber().toLowerCase().contains(query) &&
                                   !v.getModel().toLowerCase().contains(query) &&
                                   !v.getCategory().toLowerCase().contains(query));
        }

        req.setAttribute("vehicles", vehicles);
        req.getRequestDispatcher("/WEB-INF/views/vehicles.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String id = req.getParameter("id");

        try {
            if ("delete".equalsIgnoreCase(action)) {
                DataService.deleteVehicle(id);
            } else if ("create".equalsIgnoreCase(action) || "update".equalsIgnoreCase(action)) {
                boolean isNew = "create".equalsIgnoreCase(action);
                if (isNew) {
                    id = "VEH-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
                }

                String plate = req.getParameter("plateNumber");
                if (plate == null || plate.isBlank()) {
                    plate = req.getParameter("registrationNo");
                }
                String model = req.getParameter("model");
                String category = req.getParameter("category");
                String transmission = req.getParameter("transmission");
                String status = req.getParameter("status");

                if (transmission == null || transmission.isBlank()) transmission = "MANUAL";
                if (status == null || status.isBlank()) status = "AVAILABLE";

                Vehicle v = new Vehicle(id, plate, model, category, transmission, status);
                if (isNew) {
                    DataService.addVehicle(v);
                } else {
                    DataService.updateVehicle(v);
                }
            }
            resp.sendRedirect(req.getContextPath() + "/vehicles?success=true");
        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("vehicles", DataService.getVehicles());
            req.getRequestDispatcher("/WEB-INF/views/vehicles.jsp").forward(req, resp);
        }
    }
}
