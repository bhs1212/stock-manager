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

        <div class="card shadow-sm mb-4">
            <div class="card-header">📈 판매 통계</div>
            <div class="card-body">
                <ul class="nav nav-tabs mb-3" id="statTab">
                    <li class="nav-item">
                        <a class="nav-link active" data-bs-toggle="tab" href="#daily">오늘</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" data-bs-toggle="tab" href="#weekly">이번 주</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" data-bs-toggle="tab" href="#monthly">이번 달</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" data-bs-toggle="tab" href="#custom">월별 조회</a>
                    </li>
                </ul>

                <div class="tab-content">
                    <div class="tab-pane fade show active" id="daily">
                        <table class="table table-bordered">
                            <thead class="table-light">
                                <tr><th>메뉴</th><th>판매량</th></tr>
                            </thead>
                            <tbody>
                                <c:forEach var="stat" items="${dailyStats}">
                                    <tr>
                                        <td>${stat.menuName}</td>
                                        <td><strong>${stat.totalCount}개</strong></td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty dailyStats}">
                                    <tr><td colspan="2" class="text-center text-muted">오늘 판매 내역이 없습니다.</td></tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>

                    <div class="tab-pane fade" id="weekly">
                        <table class="table table-bordered">
                            <thead class="table-light">
                                <tr><th>메뉴</th><th>판매량</th></tr>
                            </thead>
                            <tbody>
                                <c:forEach var="stat" items="${weeklyStats}">
                                    <tr>
                                        <td>${stat.menuName}</td>
                                        <td><strong>${stat.totalCount}개</strong></td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty weeklyStats}">
                                    <tr><td colspan="2" class="text-center text-muted">이번 주 판매 내역이 없습니다.</td></tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>

                    <div class="tab-pane fade" id="monthly">
                        <table class="table table-bordered">
                            <thead class="table-light">
                                <tr><th>메뉴</th><th>판매량</th></tr>
                            </thead>
                            <tbody>
                                <c:forEach var="stat" items="${monthlyStats}">
                                    <tr>
                                        <td>${stat.menuName}</td>
                                        <td><strong>${stat.totalCount}개</strong></td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty monthlyStats}">
                                    <tr><td colspan="2" class="text-center text-muted">이번 달 판매 내역이 없습니다.</td></tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>

                    <div class="tab-pane fade" id="custom">
                        <form action="/sales-dashboard" method="get" class="d-flex align-items-center gap-2 mb-3">
                            <select name="year" class="form-select" style="width: 110px;">
                                <option value="2024" ${selectedYear == 2024 ? 'selected' : ''}>2024년</option>
                                <option value="2025" ${selectedYear == 2025 ? 'selected' : ''}>2025년</option>
                                <option value="2026" ${selectedYear == 2026 ? 'selected' : ''}>2026년</option>
                            </select>
                            <select name="month" class="form-select" style="width: 100px;">
                                <c:forEach var="m" begin="1" end="12">
                                    <option value="${m}" ${selectedMonth == m ? 'selected' : ''}>${m}월</option>
                                </c:forEach>
                            </select>
                            <button type="submit" class="btn btn-king">조회</button>
                        </form>

                        <table class="table table-bordered">
                            <thead class="table-light">
                                <tr><th>메뉴</th><th>판매량</th></tr>
                            </thead>
                            <tbody>
                                <c:forEach var="stat" items="${customStats}">
                                    <tr>
                                        <td>${stat.menuName}</td>
                                        <td><strong>${stat.totalCount}개</strong></td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty customStats}">
                                    <tr>
                                        <td colspan="2" class="text-center text-muted">
                                            ${selectedYear}년 ${selectedMonth}월 판매 내역이 없습니다.
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <div class="card shadow-sm">
            <div class="card-header d-flex justify-content-between align-items-center">
                <span>📋 최근 판매 내역 (최신순)</span>
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

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.has('year') || urlParams.has('month')) {
            const customTab = document.querySelector('a[href="#custom"]');
            if (customTab) {
                new bootstrap.Tab(customTab).show();
            }
        }
    </script>

</body>
</html>