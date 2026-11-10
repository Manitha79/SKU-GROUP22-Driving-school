<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="layout_header.jspf">
    <jsp:param name="title" value="Students - Apex Driving School"/>
    <jsp:param name="activeTab" value="students"/>
</jsp:include>

<!-- Viva Defense Banner -->
<div class="viva-badge-banner" style="border-left-color: #2563eb;">
    <div>
        <div class="viva-badge-title">
            <i class="bi bi-people-fill text-primary"></i> Module 1: Student Management
        </div>
        <small class="text-muted">Lead Developer: <strong>Dinushan R.K</strong> &bull; Persistence: <code>data/students.txt</code></small>
    </div>
    <div class="d-flex gap-2">
        <span class="viva-concept-pill">Inheritance (Student extends Person)</span>
        <span class="viva-concept-pill">Polymorphism (getRole)</span>
        <span class="viva-concept-pill">Encapsulation</span>
    </div>
</div>

<div class="row g-4">
    <!-- Student Registration Form -->
    <div class="col-lg-4">
        <div class="card-clean">
            <div class="card-clean-header">
                <span><i class="bi ${not empty editItem ? 'bi-pencil-square' : 'bi-person-plus'} me-1 text-primary"></i>
                ${not empty editItem ? 'Edit Student' : 'Enroll New Student'}</span>
            </div>
            <div class="card-clean-body">
                <form method="post" action="${pageContext.request.contextPath}/students">
                    <input type="hidden" name="action" value="${not empty editItem ? 'update' : 'create'}">
                    <c:if test="${not empty editItem}">
                        <input type="hidden" name="id" value="<c:out value='${editItem.id}'/>">
                        <div class="mb-3">
                            <label class="form-label-clean">Student ID</label>
                            <input class="form-control-clean w-100 bg-light" value="${editItem.id}" readonly>
                        </div>
                    </c:if>

                    <div class="mb-3">
                        <label class="form-label-clean">Full Name</label>
                        <input class="form-control-clean w-100" name="name" required placeholder="e.g. Kamal Perera" value="<c:out value='${editItem.name}'/>">
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">Email Address</label>
                        <input type="email" class="form-control-clean w-100" name="email" required placeholder="kamal@gmail.com" value="<c:out value='${editItem.email}'/>">
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">Phone Number</label>
                        <input class="form-control-clean w-100" name="phone" required placeholder="0771234567" value="<c:out value='${editItem.phone}'/>">
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">License Category</label>
                        <select class="form-select-clean w-100" name="licenseType" required>
                            <option value="Auto Car (Class B)" ${editItem.licenseType eq 'Auto Car (Class B)' ? 'selected' : ''}>Auto Car (Class B)</option>
                            <option value="Manual Car (Class B1)" ${editItem.licenseType eq 'Manual Car (Class B1)' ? 'selected' : ''}>Manual Car (Class B1)</option>
                            <option value="Motorcycle (Class A)" ${editItem.licenseType eq 'Motorcycle (Class A)' ? 'selected' : ''}>Motorcycle (Class A)</option>
                            <option value="Heavy Vehicle (Class C)" ${editItem.licenseType eq 'Heavy Vehicle (Class C)' ? 'selected' : ''}>Heavy Vehicle (Class C)</option>
                            <option value="Dual (Auto &amp; Manual)" ${editItem.licenseType eq 'Dual (Auto & Manual)' ? 'selected' : ''}>Dual (Auto &amp; Manual)</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">Address</label>
                        <textarea class="form-control-clean w-100" name="address" rows="2" required placeholder="Residential address"><c:out value='${editItem.address}'/></textarea>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn-clean-primary flex-grow-1">
                            <i class="bi bi-check-lg me-1"></i> ${not empty editItem ? 'Update Student' : 'Save Student'}
                        </button>
                        <c:if test="${not empty editItem}">
                            <a href="${pageContext.request.contextPath}/students" class="btn-clean-secondary text-decoration-none">Cancel</a>
                        </c:if>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Students Directory Table -->
    <div class="col-lg-8">
        <div class="card-clean">
            <div class="card-clean-header flex-wrap gap-2">
                <span>Enrolled Students Directory</span>
                <form class="d-flex gap-2" method="get" action="${pageContext.request.contextPath}/students">
                    <input class="form-control-clean" style="padding: 0.25rem 0.5rem; font-size: 0.8rem;" name="q" placeholder="Search name or ID..." value="<c:out value='${param.q}'/>">
                    <button class="btn-clean-primary" style="padding: 0.25rem 0.6rem; font-size: 0.8rem;"><i class="bi bi-search"></i></button>
                    <c:if test="${not empty param.q}">
                        <a href="${pageContext.request.contextPath}/students" class="btn-clean-secondary text-decoration-none" style="padding: 0.25rem 0.6rem; font-size: 0.8rem;">Clear</a>
                    </c:if>
                </form>
            </div>
            <div class="table-responsive">
                <table class="table table-clean">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Student</th>
                            <th>License</th>
                            <th>Address</th>
                            <th class="text-end">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="s" items="${students}">
                            <tr>
                                <td class="font-monospace text-muted small">${s.id}</td>
                                <td>
                                    <div class="fw-medium text-dark">${s.name}</div>
                                    <small class="text-muted">${s.phone} &bull; ${s.email}</small>
                                </td>
                                <td><span class="badge-clean badge-clean-primary">${s.licenseType}</span></td>
                                <td class="text-muted small">${s.address}</td>
                                <td class="text-end">
                                    <a href="${pageContext.request.contextPath}/students?action=edit&amp;id=${s.id}" class="btn btn-sm btn-outline-primary py-0 px-2" title="Edit">
                                        <i class="bi bi-pencil"></i>
                                    </a>
                                    <form class="d-inline" method="post" action="${pageContext.request.contextPath}/students" onsubmit="return confirm('Delete student ${s.name}?')">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${s.id}">
                                        <button type="submit" class="btn btn-sm btn-outline-danger py-0 px-2" title="Delete">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty students}">
                            <tr><td colspan="5" class="text-center py-4 text-muted">No students found.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="layout_footer.jspf"/>
