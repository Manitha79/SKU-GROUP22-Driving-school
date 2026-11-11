<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="layout_header.jspf">
    <jsp:param name="title" value="Dashboard - Apex Driving School"/>
    <jsp:param name="activeTab" value="dashboard"/>
</jsp:include>

<!-- Page Header -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h3 class="fw-bold text-dark mb-1">Executive Dashboard</h3>
        <p class="text-muted small mb-0">System overview connecting all 6 coursework modules.</p>
    </div>
    <a href="${pageContext.request.contextPath}/lessons" class="btn btn-clean-primary">
        <i class="bi bi-plus-lg me-1"></i> Book New Lesson
    </a>
</div>

<!-- 6 Minimalist KPI Cards (One for each team member) -->
<div class="row g-3 mb-4">
    <!-- Module 1: Dinushan -->
    <div class="col-6 col-md-4 col-lg-2">
        <div class="kpi-card" style="border-top: 3px solid #2563eb;">
            <div class="kpi-label">Students</div>
            <div class="kpi-value text-primary">${stats.totalStudents}</div>
            <span class="kpi-member">Dinushan R.K</span>
        </div>
    </div>
    <!-- Module 2: Thanneliyadda -->
    <div class="col-6 col-md-4 col-lg-2">
        <div class="kpi-card" style="border-top: 3px solid #d97706;">
            <div class="kpi-label">Queued</div>
            <div class="kpi-value text-warning">${stats.queuedLessons}</div>
            <span class="kpi-member">Thanneliyadda</span>
        </div>
    </div>
    <!-- Module 3: Kumarawansha -->
    <div class="col-6 col-md-4 col-lg-2">
        <div class="kpi-card" style="border-top: 3px solid #0891b2;">
            <div class="kpi-label">Instructors</div>
            <div class="kpi-value text-info">${stats.totalInstructors}</div>
            <span class="kpi-member">Kumarawansha</span>
        </div>
    </div>
    <!-- Module 4: Senevirathna -->
    <div class="col-6 col-md-4 col-lg-2">
        <div class="kpi-card" style="border-top: 3px solid #64748b;">
            <div class="kpi-label">Vehicles</div>
            <div class="kpi-value text-secondary">${stats.totalVehicles}</div>
            <span class="kpi-member">Senevirathna</span>
        </div>
    </div>
    <!-- Module 5: Weerakkody -->
    <div class="col-6 col-md-4 col-lg-2">
        <div class="kpi-card" style="border-top: 3px solid #16a34a;">
            <div class="kpi-label">Revenue</div>
            <div class="kpi-value text-success fs-5">LKR <fmt:formatNumber value="${stats.totalRevenue}" maxFractionDigits="0"/></div>
            <span class="kpi-member">Weerakkody</span>
        </div>
    </div>
    <!-- Module 6: Sajan -->
    <div class="col-6 col-md-4 col-lg-2">
        <div class="kpi-card" style="border-top: 3px solid #dc2626;">
            <div class="kpi-label">Satisfaction</div>
            <div class="kpi-value text-danger">${stats.avgRating} <span class="fs-6 text-muted">★</span></div>
            <span class="kpi-member">Sajan P.</span>
        </div>
    </div>
</div>

<!-- 2 Clean Demo Cards Side-by-Side -->
<div class="row g-4">
    <!-- Active Queue Preview (Module 2) -->
    <div class="col-lg-7">
        <div class="card-clean h-100">
            <div class="card-clean-header">
                <span><i class="bi bi-clock-history me-1 text-warning"></i> Recent Lesson Schedule</span>
                <a href="${pageContext.request.contextPath}/lessons" class="text-decoration-none small text-primary fw-medium">View Queue &rarr;</a>
            </div>
            <div class="table-responsive">
                <table class="table table-clean">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Student</th>
                            <th>Date &amp; Time</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="l" items="${recentLessons}" end="4">
                            <tr>
                                <td class="font-monospace text-muted small">${l.id}</td>
                                <td class="fw-medium text-dark">${l.studentId}</td>
                                <td>${l.lessonDate} <small class="text-muted">(${l.lessonTime})</small></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${l.status eq 'CONFIRMED'}"><span class="badge-clean badge-clean-success">Confirmed</span></c:when>
                                        <c:when test="${l.status eq 'PENDING_QUEUE'}"><span class="badge-clean badge-clean-warning">In FIFO Queue</span></c:when>
                                        <c:otherwise><span class="badge-clean badge-clean-gray">${l.status}</span></c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Instructor Ranking Preview (Module 3 - Bubble Sort) -->
    <div class="col-lg-5">
        <div class="card-clean h-100">
            <div class="card-clean-header">
                <span><i class="bi bi-trophy me-1 text-info"></i> Instructors (Bubble Sorted)</span>
                <a href="${pageContext.request.contextPath}/instructors" class="text-decoration-none small text-primary fw-medium">View Algorithm &rarr;</a>
            </div>
            <div class="card-clean-body p-0">
                <ul class="list-group list-group-flush border-0">
                    <c:forEach var="ins" items="${instructors}" end="3" varStatus="st">
                        <li class="list-group-item d-flex justify-content-between align-items-center px-3 py-2 border-bottom">
                            <div>
                                <div class="fw-medium text-dark">${ins.name}</div>
                                <small class="text-muted">${ins.licenseCategory} &bull; ${ins.phone}</small>
                            </div>
                            <span class="badge-clean badge-clean-info">
                                ${ins.experienceYears} Yrs Exp
                            </span>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>
</div>

<jsp:include page="layout_footer.jspf"/>
