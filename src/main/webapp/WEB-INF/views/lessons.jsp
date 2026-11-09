<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="layout_header.jspf">
    <jsp:param name="title" value="Lessons & Queue - Apex Driving School"/>
    <jsp:param name="activeTab" value="lessons"/>
</jsp:include>

<!-- Viva Defense Banner -->
<div class="viva-badge-banner" style="border-left-color: #d97706;">
    <div>
        <div class="viva-badge-title">
            <i class="bi bi-calendar2-check-fill text-warning"></i> Module 2: Lesson Scheduling &amp; FIFO Queue
        </div>
        <small class="text-muted">Lead Developer: <strong>Thanneliyadda T.L.K.S</strong> &bull; Persistence: <code>data/lessons.txt</code></small>
    </div>
    <div class="d-flex gap-2">
        <span class="viva-concept-pill" style="background:#fef3c7; color:#92400e;">Data Structure: LessonQueue (FIFO)</span>
        <span class="viva-concept-pill" style="background:#fef3c7; color:#92400e;">enqueue() &amp; dequeue()</span>
        <span class="viva-concept-pill" style="background:#fef3c7; color:#92400e;">Encapsulation</span>
    </div>
</div>

<!-- FIFO Queue Action Card -->
<div class="card-clean mb-4 border-start border-4 border-warning">
    <div class="card-clean-body d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 py-3">
        <div>
            <div class="d-flex align-items-center gap-2 mb-1">
                <span class="badge-clean badge-clean-warning">FIFO Queue Active</span>
                <span class="fw-bold text-dark fs-6">${queueSize} Request(s) Waiting in Queue</span>
            </div>
            <p class="text-muted small mb-0">Lessons with status <code>PENDING_QUEUE</code> are prioritized by booking order (First-In, First-Out).</p>
        </div>
        <div>
            <c:if test="${empty sessionScope.userRole or sessionScope.userRole eq 'ADMIN'}">
                <form method="post" action="${pageContext.request.contextPath}/lessons">
                    <input type="hidden" name="action" value="processQueue">
                    <button type="submit" class="btn btn-warning text-dark fw-bold btn-sm px-3 ${queueSize == 0 ? 'disabled' : ''}">
                        <i class="bi bi-play-circle-fill me-1"></i> Process Next Request in Queue
                    </button>
                </form>
            </c:if>
            <c:if test="${sessionScope.userRole eq 'STUDENT'}">
                <span class="badge bg-warning-subtle text-dark border px-3 py-2">
                    <i class="bi bi-clock-history me-1"></i> Your bookings are processed in booking order
                </span>
            </c:if>
        </div>
    </div>
</div>

<div class="row g-4">
    <!-- Schedule Form -->
    <div class="col-lg-4">
        <div class="card-clean">
            <div class="card-clean-header">
                <span><i class="bi ${not empty editItem ? 'bi-pencil-square' : 'bi-plus-circle'} me-1 text-warning"></i>
                ${not empty editItem ? 'Edit Booking' : 'Schedule Driving Lesson'}</span>
            </div>
            <div class="card-clean-body">
                <form method="post" action="${pageContext.request.contextPath}/lessons">
                    <input type="hidden" name="action" value="${not empty editItem ? 'update' : 'create'}">
                    <c:if test="${not empty editItem}">
                        <input type="hidden" name="id" value="<c:out value='${editItem.id}'/>">
                        <div class="mb-3">
                            <label class="form-label-clean">Lesson ID</label>
                            <input class="form-control-clean w-100 bg-light" value="${editItem.id}" readonly>
                        </div>
                    </c:if>

                    <div class="mb-3">
                        <label class="form-label-clean">Select Student</label>
                        <select class="form-select-clean w-100" name="studentId" required>
                            <option value="">-- Choose Student --</option>
                            <c:forEach var="s" items="${students}">
                                <option value="${s.id}" ${editItem.studentId eq s.id ? 'selected' : ''}>${s.name} (${s.id})</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">Assign Instructor</label>
                        <select class="form-select-clean w-100" name="instructorId">
                            <option value="UNASSIGNED">-- Unassigned / Auto --</option>
                            <c:forEach var="ins" items="${instructors}">
                                <option value="${ins.id}" ${editItem.instructorId eq ins.id ? 'selected' : ''}>${ins.name} (${ins.licenseCategory})</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="row g-2 mb-3">
                        <div class="col-6">
                            <label class="form-label-clean">Date</label>
                            <input type="date" class="form-control-clean w-100" name="lessonDate" required value="<c:out value='${editItem.lessonDate}'/>">
                        </div>
                        <div class="col-6">
                            <label class="form-label-clean">Time</label>
                            <input type="text" class="form-control-clean w-100" name="lessonTime" required placeholder="09:00 AM" value="<c:out value='${editItem.lessonTime}'/>">
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">Status</label>
                        <select class="form-select-clean w-100" name="status">
                            <option value="PENDING_QUEUE" ${editItem.status eq 'PENDING_QUEUE' ? 'selected' : ''}>PENDING_QUEUE (Add to FIFO Queue)</option>
                            <option value="CONFIRMED" ${editItem.status eq 'CONFIRMED' ? 'selected' : ''}>CONFIRMED</option>
                            <option value="COMPLETED" ${editItem.status eq 'COMPLETED' ? 'selected' : ''}>COMPLETED</option>
                            <option value="CANCELLED" ${editItem.status eq 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">Lesson Focus / Notes</label>
                        <textarea class="form-control-clean w-100" name="notes" rows="2" placeholder="e.g. Reverse parking practice"><c:out value='${editItem.notes}'/></textarea>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn-clean-primary flex-grow-1">
                            <i class="bi bi-check-lg me-1"></i> ${not empty editItem ? 'Update Lesson' : 'Enqueue Lesson'}
                        </button>
                        <c:if test="${not empty editItem}">
                            <a href="${pageContext.request.contextPath}/lessons" class="btn-clean-secondary text-decoration-none">Cancel</a>
                        </c:if>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Lessons Table -->
    <div class="col-lg-8">
        <div class="card-clean">
            <div class="card-clean-header flex-wrap gap-2">
                <span>All Lesson Bookings</span>
                <form class="d-flex gap-2" method="get" action="${pageContext.request.contextPath}/lessons">
                    <input class="form-control-clean" style="padding: 0.25rem 0.5rem; font-size: 0.8rem;" name="q" placeholder="Search student or ID..." value="<c:out value='${param.q}'/>">
                    <button class="btn-clean-primary" style="padding: 0.25rem 0.6rem; font-size: 0.8rem;"><i class="bi bi-search"></i></button>
                    <c:if test="${not empty param.q}">
                        <a href="${pageContext.request.contextPath}/lessons" class="btn-clean-secondary text-decoration-none" style="padding: 0.25rem 0.6rem; font-size: 0.8rem;">Clear</a>
                    </c:if>
                </form>
            </div>
            <div class="table-responsive">
                <table class="table table-clean">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Student</th>
                            <th>Instructor</th>
                            <th>Schedule</th>
                            <th>Status</th>
                            <th class="text-end">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="l" items="${lessons}">
                            <tr>
                                <td class="font-monospace text-muted small">${l.id}</td>
                                <td class="fw-medium text-dark">${l.studentId}</td>
                                <td><span class="badge-clean badge-clean-gray">${l.instructorId}</span></td>
                                <td>${l.lessonDate} <small class="text-muted">(${l.lessonTime})</small></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${l.status eq 'CONFIRMED'}"><span class="badge-clean badge-clean-success">CONFIRMED</span></c:when>
                                        <c:when test="${l.status eq 'PENDING_QUEUE'}"><span class="badge-clean badge-clean-warning">IN FIFO QUEUE</span></c:when>
                                        <c:when test="${l.status eq 'COMPLETED'}"><span class="badge-clean badge-clean-info">COMPLETED</span></c:when>
                                        <c:otherwise><span class="badge-clean badge-clean-danger">${l.status}</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-end">
                                    <c:choose>
                                        <c:when test="${empty sessionScope.userRole or sessionScope.userRole eq 'ADMIN'}">
                                            <a href="${pageContext.request.contextPath}/lessons?action=edit&amp;id=${l.id}" class="btn btn-sm btn-outline-primary py-0 px-2" title="Edit">
                                                <i class="bi bi-pencil"></i>
                                            </a>
                                            <form class="d-inline" method="post" action="${pageContext.request.contextPath}/lessons" onsubmit="return confirm('Delete lesson booking ${l.id}?')">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="id" value="${l.id}">
                                                <button type="submit" class="btn btn-sm btn-outline-danger py-0 px-2" title="Delete">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted small"><i class="bi bi-eye"></i> View</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="layout_footer.jspf"/>
