package lk.drivingschool.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lk.drivingschool.datastructure.LessonQueue;
import lk.drivingschool.model.Lesson;
import lk.drivingschool.service.DataService;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Controller for Lesson Scheduling & FIFO Queue.
 * Member 2: Thanneliyadda T.L.K.S
 * Handles booking and processing of queue requests.
 */
@WebServlet(urlPatterns = {"/lesson", "/lessons"})
public class LessonServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String id = req.getParameter("id");
        String q = req.getParameter("q");

        List<Lesson> lessons = DataService.getLessons();

        if ("edit".equalsIgnoreCase(action) && id != null) {
            for (Lesson l : lessons) {
                if (l.getId().equalsIgnoreCase(id)) {
                    req.setAttribute("editItem", l);
                    break;
                }
            }
        }

        if (q != null && !q.isBlank()) {
            String query = q.toLowerCase();
            lessons.removeIf(l -> !l.getId().toLowerCase().contains(query) &&
                                  !l.getStudentId().toLowerCase().contains(query) &&
                                  !l.getInstructorId().toLowerCase().contains(query));
        }

        LessonQueue queue = DataService.getLessonQueue();

        req.setAttribute("lessons", lessons);
        req.setAttribute("queueSize", queue.size());
        req.setAttribute("students", DataService.getStudents());
        req.setAttribute("instructors", DataService.getInstructors());
        req.getRequestDispatcher("/WEB-INF/views/lessons.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String id = req.getParameter("id");

        try {
            if ("processQueue".equalsIgnoreCase(action)) {
                // Dequeue next student in FIFO order!
                Lesson processed = DataService.processNextInQueue();
                if (processed != null) {
                    resp.sendRedirect(req.getContextPath() + "/lessons?processed=" + processed.getId());
                    return;
                }
            } else if ("delete".equalsIgnoreCase(action)) {
                DataService.deleteLesson(id);
            } else if ("create".equalsIgnoreCase(action) || "update".equalsIgnoreCase(action)) {
                boolean isNew = "create".equalsIgnoreCase(action);
                if (isNew) {
                    id = "LES-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
                }

                String studentId = req.getParameter("studentId");
                String instructorId = req.getParameter("instructorId");
                String lessonDate = req.getParameter("lessonDate");
                String lessonTime = req.getParameter("lessonTime");
                String notes = req.getParameter("notes");
                String status = req.getParameter("status");

                if (instructorId == null || instructorId.isBlank()) instructorId = "UNASSIGNED";
                if (notes == null || notes.isBlank()) notes = "Standard Road Practice";
                if (status == null || status.isBlank()) status = "PENDING_QUEUE";

                Lesson l = new Lesson(id, studentId, instructorId, lessonDate, lessonTime, notes, status);
                if (isNew) {
                    DataService.addLesson(l);
                } else {
                    DataService.updateLesson(l);
                }
            }
            resp.sendRedirect(req.getContextPath() + "/lessons?success=true");
        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("lessons", DataService.getLessons());
            req.setAttribute("queueSize", DataService.getLessonQueue().size());
            req.setAttribute("students", DataService.getStudents());
            req.setAttribute("instructors", DataService.getInstructors());
            req.getRequestDispatcher("/WEB-INF/views/lessons.jsp").forward(req, resp);
        }
    }
}
