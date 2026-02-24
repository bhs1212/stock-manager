<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>버거킹 판매 대시보드</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f4f1ea; }
        .navbar { background-color: #da291c; }
        .btn-king { background-color: #ffbc0d; color: #502314; font-weight: bold; }
        .card-header { background-color: #502314; color: white; font-weight: bold; }
    </style>
</head>
<body>

    <nav class="navbar navbar-expand-lg navbar-dark shadow-sm mb-4">
        <div class="container">
            <a class="navbar-brand" href="/inventory">🍔 BURGER KING Sales Dashboard</a>
            <div class="navbar-nav ms-auto">
                <a class="btn btn-outline-light btn-sm" href="/inventory">재고 목록으로 돌아가기</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <div class="card shadow-sm">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <span>📊 최근 판매 내역 (최신순)</span>
                        <span class="badge bg-light text-dark">총 ${salesLogs.size()}건</span>
                    </div>
                    <div class="card-body">
                        <table class="table table-hover align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th>번호</th>
                                    <th>메뉴명</th>
                                    <th>판매 수량</th>
                                    <th>판매 일시</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty salesLogs}">
                                        <c:forEach var="log" items="${salesLogs}">
                                            <tr>
                                                <td>${log.id}</td>
                                                <td><strong>${log.menuName}</strong></td>
                                                <td><span class="badge bg-primary">${log.sellCount}개</span></td>
                                                <td>
                                                    <fmt:formatDate value="${log.saleDate}" pattern="yyyy-MM-dd HH:mm:ss" />
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="4" class="text-center py-5 text-muted">
                                                아직 판매 내역이 없습니다. 🍔 메뉴를 먼저 판매해 보세요!
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>