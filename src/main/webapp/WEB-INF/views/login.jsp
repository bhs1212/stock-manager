<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>버거킹 관리자 로그인</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f4f1ea; height: 100vh; display: flex; align-items: center; justify-content: center; }
        .login-card { width: 100%; max-width: 400px; border: none; border-radius: 15px; }
        .btn-king { background-color: #da291c; color: white; font-weight: bold; }
        .btn-king:hover { background-color: #a31e15; color: white; }
    </style>
</head>
<body>
    <div class="card login-card shadow p-4">
        <div class="text-center mb-4">
            <h2 style="color: #da291c; font-weight: bold;">🍔 BURGER KING</h2>
            <p class="text-muted small">재고 관리 시스템 관리자 전용</p>
        </div>
        
        <form action="/login" method="post">
            <div class="mb-3">
                <label class="form-label">아이디</label>
                <input type="text" name="username" class="form-control" placeholder="아이디를 입력하세요" required>
            </div>
            <div class="mb-3">
                <label class="form-label">비밀번호</label>
                <input type="password" name="password" class="form-control" placeholder="비밀번호를 입력하세요" required>
            </div>
            
            <%-- 로그인 실패 시 에러 메시지 --%>
            <c:if test="${not empty error}">
                <div class="alert alert-danger py-2 small text-center">${error}</div>
            </c:if>
            
            <button type="submit" class="btn btn-king w-100 py-2 mt-2">로그인</button>
            <div style="margin-top: 15px;" class = "text-center"> 
                    <a href="/register" class="btn btn-outline-secondary">회원가입</a>
            </div>
        </form>
    </div>
</body>
</html>