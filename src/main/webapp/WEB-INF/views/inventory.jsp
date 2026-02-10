<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>버거킹 재고관리</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f4f1ea; } /* 버거킹 배경색 느낌 */
        .navbar { background-color: #da291c; } /* 버거킹 레드 */
        .btn-king { background-color: #ffbc0d; color: #502314; font-weight: bold; } /* 버거킹 옐로우 */
        .btn-king:hover { background-color: #e6a900; }
        .low-stock { color: #da291c; font-weight: bold; background-color: #ffeaea; } /* 재고 부족 강조 */
    </style>
    <style>
        /* 수량이 0일 때 글자색을 빨간색으로 만들고 굵게 표시 */
        .out-of-stock {
            color: red;
            font-weight: bold;
        }
    </style>
    <style>
        .expire-danger { background-color: #f8d7da !important; } /* 연한 빨강 (만료) */
        .expire-warning { background-color: #fff3cd !important; } /* 연한 노랑 (임박) */
    </style>
</head>
<body>

        <nav class="navbar navbar-expand-lg navbar-dark shadow-sm mb-4">
            <div class="container">
                <a class="navbar-brand" href="/inventory">🍔 BURGER KING Stock Manager</a>
        
                <div class="navbar-nav ms-auto align-items-center">
                    <c:if test="${not empty sessionScope.user}">
                        <span class="navbar-text me-3 text-white">
                            <strong>${sessionScope.user.name}</strong> 님 접속 중
                        </span>
                        <a class="btn btn-outline-light btn-sm" href="/logout">로그아웃</a>
                    </c:if>
                </div>
            </div>
        </nav>

        <div class="container">
        
            <button type="button" class="btn btn-king mb-4" data-bs-toggle="modal" data-bs-target="#salesModal">
                🍔 메뉴 판매 처리
            </button>

            <div class="card shadow-sm mb-4">
                <div class="card-header btn-king">신규 자재 등록</div>
                
                <div class="card-body">
                    <form action="/add-stock" method="post" class="row g-3">
                        <div class="col-md-3">
                            <input type="text" name="itemName" class="form-control" placeholder="자재명" required>
                        </div>
                        <div class="col-md-2">
                            <input type="number" name="quantity" class="form-control" placeholder="수량" required>
                        </div>
                        <div class="col-md-2">
                            <select name="unit" class="form-select" required>
                                <option value="" disabled selected>단위를 선택하세요</option>
                                <option value="KG">KG (킬로그램)</option>
                                <option value="EA">EA (개수)</option>
                                <option value="PACK">PACK (팩)</option>
                                <option value="BOX">BOX (박스)</option>
                                <option value="L">L (리터)</option>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <input type="date" name="expirationDate" class="form-control" required>
                        </div>
                        <div class="col-md-2">
                            <button type="submit" class="btn btn-king w-100">등록</button>
                        </div>
                    </form>
                </div>
            </div>

            <div class="card shadow-sm">
                <div class="card-body">
                <div class="mb-3">
                    <form action="/inventory" method="get" class="d-flex">
                        <input type="text" name="keyword" class="form-control me-2" placeholder="자재 이름으로 검색" value="${keyword}">
                        <button type="submit" class="btn btn-dark">검색</button>
                        <a href="/inventory" class="btn btn-outline-secondary ms-2">초기화</a>
                    </form>
                </div>
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>자재명</th><th>수량(변경)</th><th>단위</th><th>유통기한</th><th>관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${stocks}">
                                <tr class="${item.quantity < 10 ? 'low-stock' : ''}">
                                    <%-- DTO에서 계산한 남은 일수를 변수에 담기 --%>
                                    <c:set var="days" value="${item.getDaysUntilExpiration()}" />
    
                                    <%-- 조건에 따라 tr의 클래스(색상)를 결정 --%>
                                    <tr class="${days < 0 ? 'expire-danger' : (days <= 3 ? 'expire-warning' : '')}">
                                        <td>
                                            <strong>${item.itemName}</strong>
                                            <%-- 3. 이름 옆에 배지(Badge) 달아주기 --%>
                                            <c:choose>
                                                <c:when test="${days < 0}">
                                                    <span class="badge bg-danger">폐기대상</span>
                                                </c:when>
                                                <c:when test="${days <= 3}">
                                                    <span class="badge bg-warning text-dark">임박(${days}일)</span>
                                                </c:when>
                                            </c:choose>
                                        </td>
                                    <td>
                                        <%-- 권한 체크: 세션의 유저 role이 'admin'인 경우만 수정 폼 출력 --%>
                                        <c:choose>
                                            <c:when test="${sessionScope.user.role == 'admin'}">
                                                <form action="/update-stock" method="post" class="d-flex align-items-center" 
                                                    onsubmit="return confirm('수량을 변경하시겠습니까?');">
                                                    <input type="hidden" name="id" value="${item.id}">
                            
                                                    <input type="number" name="quantity" value="${item.quantity}" min="0"
                                                        class="form-control form-control-sm me-2 ${item.quantity == 0 ? 'bg-danger-subtle text-danger fw-bold' : ''}" 
                                                        style="width: 70px;">
                            
                                                    <button type="submit" class="btn btn-sm btn-outline-secondary">변경</button>
                                                </form>
                                            </c:when>
                                            <c:otherwise>
                                                <%-- 일반 사용자에게는 수량만 텍스트로 표시 --%>
                                                <span class="${item.quantity == 0 ? 'text-danger fw-bold' : ''}">${item.quantity}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${item.quantity} ${item.unit}</td>
                                    <td>
                                        <fmt:formatDate value="${item.expirationDate}" pattern="yyyy년 MM월 dd일" />
                                    </td>
                                    <td>
                                        <%-- 권한 체크: 관리자일 때만 삭제 버튼 노출 --%>
                                        <c:if test="${sessionScope.user.role == 'admin'}">
                                            <a href="/delete-stock?id=${item.id}" class="btn btn-sm btn-danger" 
                                            onclick="return confirm('정말로 삭제하시겠습니까?')">삭제</a>
                                        </c:if>
                                        <c:if test="${sessionScope.user.role != 'admin'}">
                                            <span class="text-muted small">조회 전용</span>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <div class="d-flex justify-content-center mt-4">
                        <nav>
                            <ul class="pagination">
                                <%-- 이전 버튼 --%>
                                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                    <a class="page-link" href="?page=${currentPage - 1}&keyword=${keyword}">이전</a>
                                </li>

                                <%-- 페이지 번호 --%>
                                <c:forEach var="i" begin="1" end="${totalPages}">
                                    <li class="page-item ${currentPage == i ? 'active' : ''}">
                                        <a class="page-link" href="?page=${i}&keyword=${keyword}">${i}</a>
                                    </li>
                                </c:forEach>

                                <%-- 다음 버튼 --%>
                                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <a class="page-link" href="?page=${currentPage + 1}&keyword=${keyword}">다음</a>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>
            </div>
        </div>
    </div> 
    <div class="modal fade" id="salesModal" tabindex="-1" aria-labelledby="salesModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header" style="background-color: #da291c; color: white;">
                    <h5 class="modal-title" id="salesModalLabel">🍔 메뉴 판매 (재고 자동 차감)</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="/sell-menu" method="post">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label class="form-label fw-bold">메뉴 선택</label>
                            <select name="menuName" class="form-select" required>
                                <option value="" disabled selected>판매할 메뉴를 선택하세요</option>
                                <option value="불고기버거">불고기버거</option>
                                <option value="치즈버거">치즈버거</option>
                                <option value="와퍼">와퍼</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold">판매 수량</label>
                            <div class="input-group">
                                <input type="number" name="sellCount" class="form-control" min="1" value="1" required>
                                <span class="input-group-text">개</span>
                            </div>
                        </div>
                        <div class="alert alert-info small">
                            * 판매 시 레시피에 등록된 재료 재고가 자동으로 차감됩니다.
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                        <button type="submit" class="btn btn-king">판매 확정</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        <%-- 컨트롤러에서 보낸 성공 메시지가 있으면 alert로 띄움 --%>
        <c:if test="${not empty message}">
            alert("${message}");
        </c:if>
        <%-- 에러 메시지가 있으면 alert로 띄움 --%>
        <c:if test="${not empty error}">
            alert("${error}");
        </c:if>
    </script>

</body>
</html>