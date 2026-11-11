<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="layout_header.jspf">
    <jsp:param name="title" value="Feedback - Apex Driving School"/>
    <jsp:param name="activeTab" value="feedback"/>
</jsp:include>

<!-- Viva Defense Banner -->
<div class="viva-badge-banner" style="border-left-color: #dc2626;">
    <div>
        <div class="viva-badge-title">
            <i class="bi bi-chat-square-text-fill text-danger"></i> Module 6: Admin Dashboard &amp; Feedback Management
        </div>
        <small class="text-muted">Lead Developer: <strong>Sajan P.</strong> &bull; Persistence: <code>data/feedback.txt</code></small>
    </div>
    <div class="d-flex gap-2">
        <span class="viva-concept-pill" style="background:#fee2e2; color:#b91c1c;">System Analytics</span>
        <span class="viva-concept-pill" style="background:#fee2e2; color:#b91c1c;">Satisfaction Scoring</span>
        <span class="viva-concept-pill" style="background:#fee2e2; color:#b91c1c;">Admin Quality Control</span>
    </div>
</div>

<!-- Satisfaction Score Banner -->
<div class="card-clean mb-4 border-start border-4 border-danger">
    <div class="card-clean-body d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-2 py-3">
        <div>
            <span class="badge-clean badge-clean-danger mb-1">Student Satisfaction Score</span>
            <div class="d-flex align-items-center gap-2">
                <span class="fw-bold fs-4 text-dark"><fmt:formatNumber value="${avgRating}" maxFractionDigits="1"/> / 5.0</span>
                <span class="text-warning fs-5">
                    <c:set var="stars" value="${avgRating >= 4.5 ? 5 : (avgRating >= 3.5 ? 4 : 3)}"/>
                    <c:forEach begin="1" end="${stars}">★</c:forEach><c:forEach begin="${stars+1}" end="5">☆</c:forEach>
                </span>
            </div>
        </div>
        <small class="text-muted">Monitored by Admin Sajan P. to ensure teaching and fleet quality standards.</small>
    </div>
</div>

<div class="row g-4">
    <!-- Feedback Form -->
    <div class="col-lg-4">
        <div class="card-clean">
            <div class="card-clean-header">
                <span><i class="bi ${not empty editItem ? 'bi-pencil-square' : 'bi-chat-dots'} me-1 text-danger"></i>
                ${not empty editItem ? 'Edit Feedback' : 'Submit Feedback'}</span>
            </div>
            <div class="card-clean-body">
                <form method="post" action="${pageContext.request.contextPath}/feedback">
                    <input type="hidden" name="action" value="${not empty editItem ? 'update' : 'create'}">
                    <c:if test="${not empty editItem}">
                        <input type="hidden" name="id" value="<c:out value='${editItem.id}'/>">
                        <div class="mb-3">
                            <label class="form-label-clean">Feedback ID</label>
                            <input class="form-control-clean w-100 bg-light" value="${editItem.id}" readonly>
                        </div>
                    </c:if>

                    <div class="mb-3">
                        <label class="form-label-clean">Student</label>
                        <select class="form-select-clean w-100" name="studentId" required>
                            <option value="">-- Choose Student --</option>
                            <c:forEach var="s" items="${students}">
                                <option value="${s.id}" ${editItem.studentId eq s.id ? 'selected' : ''}>${s.name} (${s.id})</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">Rating</label>
                        <select class="form-select-clean w-100 text-warning fw-bold" name="rating" required>
                            <option value="5" ${editItem.rating == 5 ? 'selected' : ''}>★★★★★ (5 Stars - Outstanding)</option>
                            <option value="4" ${editItem.rating == 4 ? 'selected' : ''}>★★★★☆ (4 Stars - Very Good)</option>
                            <option value="3" ${editItem.rating == 3 ? 'selected' : ''}>★★★☆☆ (3 Stars - Average)</option>
                            <option value="2" ${editItem.rating == 2 ? 'selected' : ''}>★★☆☆☆ (2 Stars - Needs Work)</option>
                            <option value="1" ${editItem.rating == 1 ? 'selected' : ''}>★☆☆☆☆ (1 Star - Poor)</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">Student Comments</label>
                        <textarea class="form-control-clean w-100" name="comments" rows="3" required placeholder="Write feedback regarding instructor or lessons..."><c:out value='${editItem.comments}'/></textarea>
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">Admin Review Status</label>
                        <select class="form-select-clean w-100" name="status">
                            <option value="NEW" ${editItem.status eq 'NEW' ? 'selected' : ''}>NEW (Pending Review)</option>
                            <option value="REVIEWED" ${editItem.status eq 'REVIEWED' ? 'selected' : ''}>REVIEWED</option>
                        </select>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn-clean-primary flex-grow-1">
                            <i class="bi bi-check-lg me-1"></i> ${not empty editItem ? 'Update Review' : 'Submit Review'}
                        </button>
                        <c:if test="${not empty editItem}">
                            <a href="${pageContext.request.contextPath}/feedback" class="btn-clean-secondary text-decoration-none">Cancel</a>
                        </c:if>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Feedbacks Table -->
    <div class="col-lg-8">
        <div class="card-clean">
            <div class="card-clean-header flex-wrap gap-2">
                <span>Student Reviews &amp; Feedback Roster</span>
                <form class="d-flex gap-2" method="get" action="${pageContext.request.contextPath}/feedback">
                    <input class="form-control-clean" style="padding: 0.25rem 0.5rem; font-size: 0.8rem;" name="q" placeholder="Search comments..." value="<c:out value='${param.q}'/>">
                    <button class="btn-clean-primary" style="padding: 0.25rem 0.6rem; font-size: 0.8rem;"><i class="bi bi-search"></i></button>
                    <c:if test="${not empty param.q}">
                        <a href="${pageContext.request.contextPath}/feedback" class="btn-clean-secondary text-decoration-none" style="padding: 0.25rem 0.6rem; font-size: 0.8rem;">Clear</a>
                    </c:if>
                </form>
            </div>
            <div class="table-responsive">
                <table class="table table-clean">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Student</th>
                            <th>Rating</th>
                            <th>Comments</th>
                            <th>Status</th>
                            <th class="text-end">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="fb" items="${feedbacks}">
                            <tr>
                                <td class="font-monospace text-muted small">${fb.id}</td>
                                <td class="fw-medium text-dark">${fb.studentId}</td>
                                <td><span class="text-warning fw-bold">${fb.ratingStars}</span></td>
                                <td class="small text-muted" style="max-width: 250px;">&ldquo;<c:out value="${fb.comments}"/>&rdquo;</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${fb.status eq 'REVIEWED'}"><span class="badge-clean badge-clean-success">REVIEWED</span></c:when>
                                        <c:otherwise><span class="badge-clean badge-clean-warning">NEW</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-end">
                                    <c:choose>
                                        <c:when test="${empty sessionScope.userRole or sessionScope.userRole eq 'ADMIN'}">
                                            <a href="${pageContext.request.contextPath}/feedback?action=edit&amp;id=${fb.id}" class="btn btn-sm btn-outline-primary py-0 px-2" title="Edit">
                                                <i class="bi bi-pencil"></i>
                                            </a>
                                            <form class="d-inline" method="post" action="${pageContext.request.contextPath}/feedback" onsubmit="return confirm('Delete feedback ${fb.id}?')">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="id" value="${fb.id}">
                                                <button type="submit" class="btn btn-sm btn-outline-danger py-0 px-2" title="Delete">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-light text-muted border"><i class="bi bi-chat-quote"></i> Verified Review</span>
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
