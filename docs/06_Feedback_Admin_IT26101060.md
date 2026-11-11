# 📊 Module 06: Feedback, Executive Dashboard & System Security
> **Coursework:** SLIIT – Object-Oriented Programming (OOP)  
> **Student Name:** Sajan P.  
> **Student ID:** `IT26101060`  
> **Allocated Role:** Admin Dashboard, Feedback & Security Lead  
> **Git Feature Branch:** `member-6`  

---

## 📌 1. Module Overview
The **Feedback, Dashboard & Security Module** connects all disparate components into a unified system. It is responsible for:
* **Student Feedback Engine:** Capturing 5-star ratings, student comments, and computing satisfaction indices.
* **Executive Dashboard:** Live analytical center displaying KPI summaries across all 6 modules.
* **Authentication & Authorization (`AuthFilter`):** Intercepting HTTP requests, maintaining user sessions, and enforcing role-based permissions (`ADMIN`, `INSTRUCTOR`, `STUDENT`).
* **Design System:** Standardizing global navigation, headers, footers, responsive tables, and typography.

---

## 📂 2. Key Files Contributed
| File Path | Component Type | Responsibility |
|:---|:---|:---|
| `src/main/java/lk/drivingschool/model/Feedback.java` | Domain Entity | Encapsulates student reviews, ratings (1–5 stars), feedback dates, statuses. |
| `src/main/java/lk/drivingschool/model/UserRole.java` | Role Definition | Enum/Class defining permission matrices for Admin, Instructor, and Student. |
| `src/main/java/lk/drivingschool/servlet/AuthFilter.java` | Security Filter | Intercepts all incoming requests; protects authenticated routes. |
| `src/main/java/lk/drivingschool/servlet/AuthServlet.java` | Security Controller | Handles login, logout, credential authentication, and session generation. |
| `src/main/java/lk/drivingschool/servlet/FeedbackServlet.java` | Controller | CRUD for feedback, review moderation, and average rating computation. |
| `src/main/java/lk/drivingschool/servlet/HomeServlet.java` | Dashboard Controller | Queries statistics from all 6 modules and feeds the dashboard. |
| `src/main/webapp/WEB-INF/views/dashboard.jsp` | View Layer (JSP) | Live executive KPI cards, recent lesson queue preview, instructor ranks. |
| `src/main/webapp/WEB-INF/views/feedback.jsp` | View Layer (JSP) | Star ratings UI, customer testimonials, admin moderation panel. |
| `src/main/webapp/css/style.css` | Styling Engine | Unified design variables, badges, buttons, responsive cards. |

---

## 💡 3. Security & Filter Implementation (`AuthFilter.java`)

### Intercepting Filter Pattern:
Every request to protected endpoints (`/students`, `/instructors`, `/lessons`, `/vehicles`, `/payments`, `/feedback`, `/dashboard`) passes through `AuthFilter`:

```java
@WebFilter("/*")
public class AuthFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String path = req.getRequestURI();
        boolean isLoggedIn = (session != null && session.getAttribute("userRole") != null);

        // Allow public assets and login endpoints
        if (path.contains("/login") || path.contains("/css/") || path.contains("/images/")) {
            chain.doFilter(request, response);
            return;
        }

        if (isLoggedIn) {
            chain.doFilter(request, response); // Proceed to requested servlet
        } else {
            res.sendRedirect(req.getContextPath() + "/login"); // Redirect unauthorized
        }
    }
}
```

---

## 💡 4. Mathematical Analytics & Aggregation
The average customer satisfaction rating is computed dynamically:
$$\text{Average Rating} = \frac{\sum_{i=1}^{N} \text{Rating}_i}{N}$$
Displayed on the executive dashboard to monitor service quality.

---

## 🧪 5. Testing & Viva Walkthrough
1. **Navigate to:** `http://localhost:8080/DrivingSchoolSystem/login`
2. **Login as Admin:** Username: `admin`, Password: `admin123`.
3. **Verify Dashboard:** Check all 6 KPI cards (Students, Queued, Instructors, Vehicles, Revenue, Satisfaction).
4. **Submit Feedback:** Go to `/feedback`, select student `STU-1003`, rate `5 Stars`, enter *"Excellent instructor patience!"* and submit.
5. Notice that the average rating and dashboard statistics update accordingly.

---

## 🎯 6. Expected Viva Questions & Answers
* **Q1: Why did you use a Filter (`AuthFilter`) instead of checking login in every servlet?**  
  * *Answer:* To implement the Intercepting Filter pattern. It centralizes access control in one place rather than duplicating session checks across 7 individual servlets.
* **Q2: How does the Dashboard aggregate data from all other 5 modules?**  
  * *Answer:* The `HomeServlet` calls `DataService`, which loads records from `students.txt`, `lessons.txt`, `instructors.txt`, `vehicles.txt`, `payments.txt`, and `feedback.txt`, computes summary counts, and forwards them in the request scope.
