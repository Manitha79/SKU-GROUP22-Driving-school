<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Sign In - Apex Driving School</title>
    <!-- Bootstrap 5.3 CSS & Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <style>
        body {
            background-color: #f1f5f9;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .login-card {
            background: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.06);
            border: 1px solid #e2e8f0;
            max-width: 460px;
            width: 100%;
            padding: 2.25rem;
        }
        .role-option {
            border: 1.5px solid #e2e8f0;
            border-radius: 10px;
            padding: 1rem;
            margin-bottom: 0.75rem;
            cursor: pointer;
            transition: all 0.2s ease;
            display: flex;
            align-items: center;
            gap: 1rem;
        }
        .role-option:hover {
            border-color: #2563eb;
            background-color: #eff6ff;
        }
        .role-option input[type="radio"] {
            transform: scale(1.2);
        }
    </style>
</head>
<body>

<div class="login-card">
    <div class="text-center mb-4">
        <div class="d-inline-flex align-items-center justify-content-center bg-primary text-white rounded-circle mb-2" style="width: 52px; height: 52px;">
            <i class="bi bi-car-front-fill fs-4"></i>
        </div>
        <h4 class="fw-bold text-dark mb-1">Apex Driving School</h4>
        <p class="text-muted small mb-0">Select your role to access the relevant portal</p>
    </div>

    <c:if test="${not empty param.msg}">
        <div class="alert alert-success alert-dismissible fade show py-2 small" role="alert">
            <c:out value="${param.msg}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/login">
        <!-- Role 1: Admin -->
        <label class="role-option w-100">
            <input type="radio" name="role" value="ADMIN" checked>
            <div class="flex-grow-1">
                <div class="fw-bold text-dark d-flex align-items-center justify-content-between">
                    <span><i class="bi bi-shield-lock-fill text-danger me-1"></i> Administrator Portal</span>
                    <span class="badge bg-danger-subtle text-danger small">Full Access</span>
                </div>
                <small class="text-muted d-block">Manage all 6 modules, view financial KPIs, audit data</small>
            </div>
        </label>

        <!-- Role 2: Student -->
        <label class="role-option w-100">
            <input type="radio" name="role" value="STUDENT">
            <div class="flex-grow-1">
                <div class="fw-bold text-dark d-flex align-items-center justify-content-between">
                    <span><i class="bi bi-person-fill text-primary me-1"></i> Student / Customer Portal</span>
                    <span class="badge bg-primary-subtle text-primary small">Customer</span>
                </div>
                <small class="text-muted d-block">Book driving lessons, pay fees, submit reviews</small>
            </div>
        </label>

        <!-- Role 3: Instructor -->
        <label class="role-option w-100">
            <input type="radio" name="role" value="INSTRUCTOR">
            <div class="flex-grow-1">
                <div class="fw-bold text-dark d-flex align-items-center justify-content-between">
                    <span><i class="bi bi-person-badge-fill text-info me-1"></i> Instructor Portal</span>
                    <span class="badge bg-info-subtle text-info small">Staff</span>
                </div>
                <small class="text-muted d-block">View scheduled training sessions, fleet, and rankings</small>
            </div>
        </label>

        <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold mt-3">
            <i class="bi bi-box-arrow-in-right me-1"></i> Enter Selected Portal
        </button>
    </form>

    <div class="text-center mt-4 pt-3 border-top">
        <small class="text-muted">
            OOP Coursework Demonstration &bull; RBAC Module
        </small>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
