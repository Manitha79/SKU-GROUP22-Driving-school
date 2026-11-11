<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="layout_header.jspf">
    <jsp:param name="title" value="Instructors - Apex Driving School"/>
    <jsp:param name="activeTab" value="instructors"/>
</jsp:include>

<!-- Viva Defense Banner -->
<div class="viva-badge-banner" style="border-left-color: #0891b2;">
    <div>
        <div class="viva-badge-title">
            <i class="bi bi-person-badge-fill text-info"></i> Module 3: Instructor Management &amp; Bubble Sort
        </div>
        <small class="text-muted">Lead Developer: <strong>Kumarawansha W.K.R.D</strong> &bull; Persistence: <code>data/instructors.txt</code></small>
    </div>
    <div class="d-flex gap-2">
        <span class="viva-concept-pill" style="background:#cffafe; color:#0e7490;">Algorithm: BubbleSort</span>
        <span class="viva-concept-pill" style="background:#cffafe; color:#0e7490;">Inheritance (Instructor extends Person)</span>
        <span class="viva-concept-pill" style="background:#cffafe; color:#0e7490;">Polymorphism (getRole)</span>
    </div>
</div>

<!-- Bubble Sort Live Control Card -->
<div class="card-clean mb-4 border-start border-4 border-info">
    <div class="card-clean-body d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 py-3">
        <div>
            <div class="d-flex align-items-center gap-2 mb-1">
                <span class="badge-clean badge-clean-info">Bubble Sort Active</span>
                <span class="fw-bold text-dark fs-6">
                    Sorted by Experience: <strong>${sortOrder eq 'asc' ? 'Ascending (Lowest First)' : 'Descending (Highest First)'}</strong>
                </span>
            </div>
            <p class="text-muted small mb-0">Demonstrating adjacent swaps on <code>experienceYears</code> with early-exit optimization.</p>
        </div>
        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/instructors?sort=desc" class="btn btn-sm ${sortOrder ne 'asc' ? 'btn-info text-dark fw-bold' : 'btn-clean-secondary text-decoration-none'}">
                <i class="bi bi-sort-numeric-down-alt me-1"></i> Highest Experience First
            </a>
            <a href="${pageContext.request.contextPath}/instructors?sort=asc" class="btn btn-sm ${sortOrder eq 'asc' ? 'btn-info text-dark fw-bold' : 'btn-clean-secondary text-decoration-none'}">
                <i class="bi bi-sort-numeric-up me-1"></i> Lowest Experience First
            </a>
        </div>
    </div>
</div>

<div class="row g-4">
    <!-- Instructor Form (Admin Only) -->
    <c:if test="${empty sessionScope.userRole or sessionScope.userRole eq 'ADMIN'}">
    <div class="col-lg-4">
        <div class="card-clean">
            <div class="card-clean-header">
                <span><i class="bi ${not empty editItem ? 'bi-pencil-square' : 'bi-person-plus'} me-1 text-info"></i>
                ${not empty editItem ? 'Edit Instructor' : 'Add New Instructor'}</span>
            </div>
            <div class="card-clean-body">
                <form method="post" action="${pageContext.request.contextPath}/instructors">
                    <input type="hidden" name="action" value="${not empty editItem ? 'update' : 'create'}">
                    <c:if test="${not empty editItem}">
                        <input type="hidden" name="id" value="<c:out value='${editItem.id}'/>">
                        <div class="mb-3">
                            <label class="form-label-clean">Instructor ID</label>
                            <input class="form-control-clean w-100 bg-light" value="${editItem.id}" readonly>
                        </div>
                    </c:if>

                    <div class="mb-3">
                        <label class="form-label-clean">Instructor Name</label>
                        <input class="form-control-clean w-100" name="name" required placeholder="e.g. Sunil Bandara" value="<c:out value='${editItem.name}'/>">
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">Phone Number</label>
                        <input class="form-control-clean w-100" name="phone" required placeholder="0777112233" value="<c:out value='${editItem.phone}'/>">
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">Email Address</label>
                        <input type="email" class="form-control-clean w-100" name="email" placeholder="sunil@drivingschool.lk" value="<c:out value='${editItem.email}'/>">
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">Specialized Category</label>
                        <select class="form-select-clean w-100" name="licenseCategory" required>
                            <option value="Car &amp; Van" ${editItem.licenseCategory eq 'Car & Van' ? 'selected' : ''}>Car &amp; Van</option>
                            <option value="Motorcycle" ${editItem.licenseCategory eq 'Motorcycle' ? 'selected' : ''}>Motorcycle</option>
                            <option value="Heavy Vehicles" ${editItem.licenseCategory eq 'Heavy Vehicles' ? 'selected' : ''}>Heavy Vehicles</option>
                            <option value="Dual Purpose" ${editItem.licenseCategory eq 'Dual Purpose' ? 'selected' : ''}>Dual Purpose</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">Experience (Years)</label>
                        <input type="number" class="form-control-clean w-100" name="experienceYears" required min="0" placeholder="e.g. 15" value="<c:out value='${editItem.experienceYears}'/>">
                        <small class="text-muted" style="font-size: 0.75rem;">Used as the sorting key in BubbleSort.</small>
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">Status</label>
                        <select class="form-select-clean w-100" name="status">
                            <option value="ACTIVE" ${editItem.status eq 'ACTIVE' ? 'selected' : ''}>ACTIVE</option>
                            <option value="ON_LEAVE" ${editItem.status eq 'ON_LEAVE' ? 'selected' : ''}>ON_LEAVE</option>
                        </select>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn-clean-primary flex-grow-1">
                            <i class="bi bi-check-lg me-1"></i> ${not empty editItem ? 'Update Instructor' : 'Save Instructor'}
                        </button>
                        <c:if test="${not empty editItem}">
                            <a href="${pageContext.request.contextPath}/instructors" class="btn-clean-secondary text-decoration-none">Cancel</a>
                        </c:if>
                    </div>
                </form>
            </div>
        </div>
    </div>
    </c:if>

    <!-- Instructors Table -->
    <div class="${sessionScope.userRole eq 'INSTRUCTOR' ? 'col-lg-12' : 'col-lg-8'}">
        <div class="card-clean">
            <div class="card-clean-header flex-wrap gap-2">
                <span>Instructor Staff Directory (Bubble Sorted)</span>
                <form class="d-flex gap-2" method="get" action="${pageContext.request.contextPath}/instructors">
                    <input class="form-control-clean" style="padding: 0.25rem 0.5rem; font-size: 0.8rem;" name="q" placeholder="Search instructor..." value="<c:out value='${param.q}'/>">
                    <button class="btn-clean-primary" style="padding: 0.25rem 0.6rem; font-size: 0.8rem;"><i class="bi bi-search"></i></button>
                    <c:if test="${not empty param.q}">
                        <a href="${pageContext.request.contextPath}/instructors" class="btn-clean-secondary text-decoration-none" style="padding: 0.25rem 0.6rem; font-size: 0.8rem;">Clear</a>
                    </c:if>
                </form>
            </div>
            <div class="table-responsive">
                <table class="table table-clean">
                    <thead>
                        <tr>
                            <th>Rank</th>
                            <th>Instructor</th>
                            <th>Category</th>
                            <th>Experience</th>
                            <th>Status</th>
                            <th class="text-end">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="ins" items="${instructors}" varStatus="st">
                            <tr>
                                <td>
                                    <span class="badge ${st.index == 0 ? 'bg-warning text-dark' : 'bg-light text-secondary'} rounded-circle px-2 py-1">
                                        #${st.index + 1}
                                    </span>
                                </td>
                                <td>
                                    <div class="fw-medium text-dark">${ins.name}</div>
                                    <small class="text-muted">${ins.phone} &bull; ${ins.email}</small>
                                </td>
                                <td><span class="badge-clean badge-clean-primary">${ins.licenseCategory}</span></td>
                                <td><span class="badge-clean badge-clean-info">${ins.experienceYears} Years</span></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${ins.status eq 'ACTIVE'}"><span class="badge-clean badge-clean-success">Active</span></c:when>
                                        <c:otherwise><span class="badge-clean badge-clean-warning">On Leave</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-end">
                                    <c:choose>
                                        <c:when test="${empty sessionScope.userRole or sessionScope.userRole eq 'ADMIN'}">
                                            <a href="${pageContext.request.contextPath}/instructors?action=edit&amp;id=${ins.id}" class="btn btn-sm btn-outline-primary py-0 px-2" title="Edit">
                                                <i class="bi bi-pencil"></i>
                                            </a>
                                            <form class="d-inline" method="post" action="${pageContext.request.contextPath}/instructors" onsubmit="return confirm('Delete instructor ${ins.name}?')">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="id" value="${ins.id}">
                                                <button type="submit" class="btn btn-sm btn-outline-danger py-0 px-2" title="Delete">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-light text-muted border"><i class="bi bi-person-check"></i> Staff</span>
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
