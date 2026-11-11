<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="layout_header.jspf">
    <jsp:param name="title" value="Payments - Apex Driving School"/>
    <jsp:param name="activeTab" value="payments"/>
</jsp:include>

<!-- Viva Defense Banner -->
<div class="viva-badge-banner" style="border-left-color: #16a34a;">
    <div>
        <div class="viva-badge-title">
            <i class="bi bi-credit-card-fill text-success"></i> Module 5: Payment &amp; Billing Management
        </div>
        <small class="text-muted">Lead Developer: <strong>Weerakkody M.R.B</strong> &bull; Persistence: <code>data/payments.txt</code></small>
    </div>
    <div class="d-flex gap-2">
        <span class="viva-concept-pill" style="background:#dcfce7; color:#15803d;">Encapsulation</span>
        <span class="viva-concept-pill" style="background:#dcfce7; color:#15803d;">Revenue Aggregation</span>
        <span class="viva-concept-pill" style="background:#dcfce7; color:#15803d;">Financial Invoicing</span>
    </div>
</div>

<!-- Revenue Summary Card (Admin Only) -->
<c:if test="${empty sessionScope.userRole or sessionScope.userRole eq 'ADMIN'}">
<div class="card-clean mb-4 border-start border-4 border-success">
    <div class="card-clean-body d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-2 py-3">
        <div>
            <span class="badge-clean badge-clean-success mb-1">Total Verified Revenue (Admin Executive View)</span>
            <div class="fw-bold fs-4 text-dark">
                LKR <fmt:formatNumber value="${totalRevenue}" minFractionDigits="2" maxFractionDigits="2"/>
            </div>
        </div>
        <small class="text-muted">Calculated by iterating through payments where <code>status = "PAID"</code>.</small>
    </div>
</div>
</c:if>
<c:if test="${sessionScope.userRole eq 'STUDENT'}">
<div class="card-clean mb-4 border-start border-4 border-primary">
    <div class="card-clean-body py-3">
        <h6 class="fw-bold text-dark mb-1"><i class="bi bi-wallet2 text-primary me-1"></i> Student Payment Portal</h6>
        <small class="text-muted">Submit course payments online or view receipts issued for your driving lessons.</small>
    </div>
</div>
</c:if>

<div class="row g-4">
    <!-- Payment Form -->
    <div class="col-lg-4">
        <div class="card-clean">
            <div class="card-clean-header">
                <span><i class="bi ${not empty editItem ? 'bi-pencil-square' : 'bi-cash-coin'} me-1 text-success"></i>
                ${not empty editItem ? 'Edit Payment' : 'Issue Receipt'}</span>
            </div>
            <div class="card-clean-body">
                <form method="post" action="${pageContext.request.contextPath}/payments">
                    <input type="hidden" name="action" value="${not empty editItem ? 'update' : 'create'}">
                    <c:if test="${not empty editItem}">
                        <input type="hidden" name="id" value="<c:out value='${editItem.id}'/>">
                        <div class="mb-3">
                            <label class="form-label-clean">Receipt No</label>
                            <input class="form-control-clean w-100 bg-light" value="${editItem.id}" readonly>
                        </div>
                    </c:if>

                    <div class="mb-3">
                        <label class="form-label-clean">Paying Student</label>
                        <select class="form-select-clean w-100" name="studentId" required>
                            <option value="">-- Choose Student --</option>
                            <c:forEach var="s" items="${students}">
                                <option value="${s.id}" ${editItem.studentId eq s.id ? 'selected' : ''}>${s.name} (${s.id})</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">Amount (LKR)</label>
                        <input type="number" step="0.50" min="1" class="form-control-clean w-100" name="amount" required placeholder="e.g. 25000" value="<c:out value='${editItem.amount}'/>">
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">Payment Date</label>
                        <input type="date" class="form-control-clean w-100" name="paymentDate" value="<c:out value='${editItem.paymentDate}'/>">
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">Payment Method</label>
                        <select class="form-select-clean w-100" name="method">
                            <option value="CARD" ${editItem.method eq 'CARD' ? 'selected' : ''}>Credit / Debit Card</option>
                            <option value="CASH" ${editItem.method eq 'CASH' ? 'selected' : ''}>Cash Payment</option>
                            <option value="BANK_TRANSFER" ${editItem.method eq 'BANK_TRANSFER' ? 'selected' : ''}>Bank Transfer / Wire</option>
                            <option value="ONLINE" ${editItem.method eq 'ONLINE' ? 'selected' : ''}>Online Gateway</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label class="form-label-clean">Status</label>
                        <select class="form-select-clean w-100" name="status">
                            <option value="PAID" ${editItem.status eq 'PAID' ? 'selected' : ''}>PAID (Settled)</option>
                            <option value="PENDING" ${editItem.status eq 'PENDING' ? 'selected' : ''}>PENDING (Verification)</option>
                        </select>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn-clean-primary flex-grow-1">
                            <i class="bi bi-check-lg me-1"></i> ${not empty editItem ? 'Update Invoice' : 'Issue Receipt'}
                        </button>
                        <c:if test="${not empty editItem}">
                            <a href="${pageContext.request.contextPath}/payments" class="btn-clean-secondary text-decoration-none">Cancel</a>
                        </c:if>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Payments Ledger Table -->
    <div class="col-lg-8">
        <div class="card-clean">
            <div class="card-clean-header flex-wrap gap-2">
                <span>Receipts &amp; Billing Ledger</span>
                <form class="d-flex gap-2" method="get" action="${pageContext.request.contextPath}/payments">
                    <input class="form-control-clean" style="padding: 0.25rem 0.5rem; font-size: 0.8rem;" name="q" placeholder="Search receipt or student..." value="<c:out value='${param.q}'/>">
                    <button class="btn-clean-primary" style="padding: 0.25rem 0.6rem; font-size: 0.8rem;"><i class="bi bi-search"></i></button>
                    <c:if test="${not empty param.q}">
                        <a href="${pageContext.request.contextPath}/payments" class="btn-clean-secondary text-decoration-none" style="padding: 0.25rem 0.6rem; font-size: 0.8rem;">Clear</a>
                    </c:if>
                </form>
            </div>
            <div class="table-responsive">
                <table class="table table-clean">
                    <thead>
                        <tr>
                            <th>Receipt</th>
                            <th>Student</th>
                            <th>Amount</th>
                            <th>Method</th>
                            <th>Status</th>
                            <th class="text-end">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${payments}">
                            <tr>
                                <td class="font-monospace text-muted small">${p.id}</td>
                                <td class="fw-medium text-dark">${p.studentId}</td>
                                <td class="fw-bold text-dark">LKR <fmt:formatNumber value="${p.amount}" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td><span class="badge-clean badge-clean-gray">${p.method}</span></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${p.status eq 'PAID'}"><span class="badge-clean badge-clean-success">PAID</span></c:when>
                                        <c:otherwise><span class="badge-clean badge-clean-warning">PENDING</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-end">
                                    <c:choose>
                                        <c:when test="${empty sessionScope.userRole or sessionScope.userRole eq 'ADMIN'}">
                                            <a href="${pageContext.request.contextPath}/payments?action=edit&amp;id=${p.id}" class="btn btn-sm btn-outline-primary py-0 px-2" title="Edit">
                                                <i class="bi bi-pencil"></i>
                                            </a>
                                            <form class="d-inline" method="post" action="${pageContext.request.contextPath}/payments" onsubmit="return confirm('Delete receipt ${p.id}?')">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="id" value="${p.id}">
                                                <button type="submit" class="btn btn-sm btn-outline-danger py-0 px-2" title="Delete">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-light text-dark border"><i class="bi bi-check2-circle text-success me-1"></i> Recorded</span>
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
