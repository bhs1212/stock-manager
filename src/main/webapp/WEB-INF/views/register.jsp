<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원가입 | 재고관리 시스템</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f4f1ea; height: 100vh; display: flex; align-items: center; justify-content: center; }
        .register-container {margin-top: 100px; max-width: 450px;}
        .card {border: none; border-radius: 10px; box-shadow: 0 4px 10px rgba(0,0,0,0.1);}
        .card-header {background-color: #da291c; color: white; border-radius: 10px 10px 0 0 !important; padding: 20px;}
        .btn-primary {background-color: #da291c; border: none; padding: 10px;}
        .btn-primary:hover {background-color: #0b5ed7;}
    </style>
</head>
<body>

<div class="container d-flex justify-content-center">
    <div class="register-container w-100">
        
        <div class="card">
            <div class="card-header text-center">
                <h4 class="mb-0">신규 계정 등록</h4>
            </div>
            <div class="card-body p-4">
                
                <%-- 에러 발생 시에만 나타나는 경고창 (공백 없이 작성) --%>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger mb-4" role="alert" style="font-size: 0.9rem;">
                        <i class="bi bi-exclamation-circle-fill me-2"></i>${error}
                    </div>
                </c:if>

                <form action="/register" method="post">
                    <div class="mb-3">
                        <label class="form-label text-secondary">아이디</label>
                        <input type="text" name="username" class="form-control" placeholder="아이디를 입력하세요" required>
                    </div>
                    
                    <div class="mb-3">
                        <label class="form-label text-secondary">비밀번호</label>
                        <input type="password" name="password" class="form-control" placeholder="비밀번호를 입력하세요" required>
                    </div>

                    <div class="mb-4">
                        <label class="form-label text-secondary">이름</label>
                        <input type="text" name="name" class="form-control" placeholder="성함을 입력하세요" required>
                    </div>

                    <%-- role은 기본적으로 user로 설정 --%>
                    <input type="hidden" name="role" value="user">

                    <button type="submit" class="btn btn-primary w-100 mb-3">가입 신청</button>
                    
                    <div class="text-center">
                        <small class="text-muted">이미 계정이 있으신가요? </small>
                        <a href="/login" class="text-decoration-none small">로그인하기</a>
                    </div>
                </form>

            </div>
        </div>

    </div>
</div>

</body>
</html>