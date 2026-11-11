<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="layout_header.jspf">
    <jsp:param name="title" value="Vehicles - Apex Driving School"/>
    <jsp:param name="activeTab" value="vehicles"/>
</jsp:include>

<!-- Viva Defense Banner -->
<div class="viva-badge-banner" style="border-left-color: #64748b;">
    <div>
        <div class="viva-badge-title">
            <i class="bi bi-truck text-secondary"></i> Module 4: Vehicle Fleet Management
        </div>
        <small class="text-muted">Lead Developer: <strong>Senevirathna M.G.T.R</strong> &bull; Persistence: <code>data/vehicles.txt</code></small>
    </div>
    <div class="d-flex gap-2">
        <span class="viva-concept-pill" style="background:#f1f5f9; color:#475569;">Encapsulation</span>
        <span class="viva-concept-pill" style="background:#f1f5f9; color:#475569;">Fleet State Machine</span>
        <span class="viva-concept-pill" style="background:#f1f5f9; color:#475569;">Asset Readiness</span>
    </div>
</div>

<div class="row g-4">
    <!-- Vehicle Form (Admin Only) -->
    <c:if test="${empty sessionScope.userRole or sessionScope.userRole eq 'ADMIN'}">
    <div class="col-lg-4">
        <div class="card-clean">
            <div class="card-clean-header">
                <span><i class="bi ${not empty editItem ? 'bi-pencil-square' : 'bi-plus-circle'} me-1 text-secondary"></i>
                ${not empty editItem ? 'Edit Vehicle' : 'Register Vehicle'}</span>
            </div>
            <div class="card-clean-body">
                <form method="post" action="${pageContext.request.contextPath}/vehicles">
                    <input type="hidden" name="action" value="${not empty editItem ? 'update' : 'create'}">
                    <c:if test="${not empty editItem}">
                        <input type="hidden" name="id" value="<c:out value='${editItem.id}'/>">
                        <div class="mb-3">
                            <label class="form-label-clean">Vehicle System ID</label>
                            <input class="form-control-clean w-100 bg-light" value="${editItem.id}" readonly>
                        </div>
                    </c:if>

                    <div class="mb-3">
                        <label class="form-label-clean">Registration Plate Number</label>
                        <input class="form-control-clean w-100 font-monospace text-uppercase" name="plateNumber" required placeholder="e.g. WP CAB-1234" value="<c:out value='${editItem.plateNumber}'/>">
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">Make &amp; Model</label>
                        <input class="form-control-clean w-100" name="model" required placeholder="e.g. Toyota Vitz 2021" value="<c:out value='${editItem.model}'/>">
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">Vehicle Category</label>
                        <select class="form-select-clean w-100" name="category" required>
                            <option value="Car (Auto)" ${editItem.category eq 'Car (Auto)' ? 'selected' : ''}>Car (Auto)</option>
                            <option value="Car (Manual)" ${editItem.category eq 'Car (Manual)' ? 'selected' : ''}>Car (Manual)</option>
                            <option value="Motorcycle" ${editItem.category eq 'Motorcycle' ? 'selected' : ''}>Motorcycle</option>
                            <option value="Heavy Vehicle" ${editItem.category eq 'Heavy Vehicle' ? 'selected' : ''}>Heavy Vehicle (Bus/Lorry)</option>
                            <option value="Van" ${editItem.category eq 'Van' ? 'selected' : ''}>Van</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">Transmission</label>
                        <select class="form-select-clean w-100" name="transmission">
                            <option value="AUTOMATIC" ${editItem.transmission eq 'AUTOMATIC' ? 'selected' : ''}>AUTOMATIC</option>
                            <option value="MANUAL" ${editItem.transmission eq 'MANUAL' ? 'selected' : ''}>MANUAL</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">Current Fleet Status</label>
                        <select class="form-select-clean w-100" name="status">
                            <option value="AVAILABLE" ${editItem.status eq 'AVAILABLE' ? 'selected' : ''}>AVAILABLE (Ready for Lessons)</option>
                            <option value="IN_LESSON" ${editItem.status eq 'IN_LESSON' ? 'selected' : ''}>IN_LESSON</option>
                            <option value="MAINTENANCE" ${editItem.status eq 'MAINTENANCE' ? 'selected' : ''}>MAINTENANCE / SERVICE</option>
                        </select>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn-clean-primary flex-grow-1">
                            <i class="bi bi-check-lg me-1"></i> ${not empty editItem ? 'Update Vehicle' : 'Save Vehicle'}
                        </button>
                        <c:if test="${not empty editItem}">
                            <a href="${pageContext.request.contextPath}/vehicles" class="btn-clean-secondary text-decoration-none">Cancel</a>
                        </c:if>
                    </div>
                </form>
            </div>
        </div>
    </div>
    </c:if>

    <!-- Vehicles Table -->
    <div class="${sessionScope.userRole eq 'INSTRUCTOR' ? 'col-lg-12' : 'col-lg-8'}">
        <div class="card-clean">
            <div class="card-clean-header flex-wrap gap-2">
                <span>Fleet Inventory Roster</span>
                <form class="d-flex gap-2" method="get" action="${pageContext.request.contextPath}/vehicles">
                    <input class="form-control-clean" style="padding: 0.25rem 0.5rem; font-size: 0.8rem;" name="q" placeholder="Search plate or model..." value="<c:out value='${param.q}'/>">
                    <button class="btn-clean-primary" style="padding: 0.25rem 0.6rem; font-size: 0.8rem;"><i class="bi bi-search"></i></button>
                    <c:if test="${not empty param.q}">
                        <a href="${pageContext.request.contextPath}/vehicles" class="btn-clean-secondary text-decoration-none" style="padding: 0.25rem 0.6rem; font-size: 0.8rem;">Clear</a>
                    </c:if>
                </form>
            </div>
            <div class="table-responsive">
                <table class="table table-clean">
                    <thead>
                        <tr>
                            <th>Plate No</th>
                            <th>Model</th>
                            <th>Category</th>
                            <th>Transmission</th>
                            <th>Status</th>
                            <th class="text-end">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="v" items="${vehicles}">
                            <tr>
                                <td class="font-monospace fw-bold text-dark">${v.plateNumber}</td>
                                <td>${v.model}</td>
                                <td><span class="badge-clean badge-clean-primary">${v.category}</span></td>
                                <td><span class="badge-clean badge-clean-gray">${v.transmission}</span></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${v.status eq 'AVAILABLE'}"><span class="badge-clean badge-clean-success">Available</span></c:when>
                                        <c:when test="${v.status eq 'IN_LESSON'}"><span class="badge-clean badge-clean-warning">In Lesson</span></c:when>
                                        <c:otherwise><span class="badge-clean badge-clean-danger">Maintenance</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-end">
                                    <c:choose>
                                        <c:when test="${empty sessionScope.userRole or sessionScope.userRole eq 'ADMIN'}">
                                            <a href="${pageContext.request.contextPath}/vehicles?action=edit&amp;id=${v.id}" class="btn btn-sm btn-outline-primary py-0 px-2" title="Edit">
                                                <i class="bi bi-pencil"></i>
                                            </a>
                                            <form class="d-inline" method="post" action="${pageContext.request.contextPath}/vehicles" onsubmit="return confirm('Delete vehicle ${v.plateNumber}?')">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="id" value="${v.id}">
                                                <button type="submit" class="btn btn-sm btn-outline-danger py-0 px-2" title="Delete">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-light text-muted border"><i class="bi bi-truck"></i> Fleet Unit</span>
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
