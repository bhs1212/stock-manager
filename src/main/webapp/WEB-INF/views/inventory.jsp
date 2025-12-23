<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %> 
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>재고 관리</title>
</head>
<body>
    <h2>🍔 버거킹 재고 관리 시스템 🍔</h2>

    <div style="background-color: #f9f9f9; padding: 15px; border: 1px solid #ddd; margin-bottom: 20px;">
        <h3>[ 신규 자재 등록 ]</h3>
        <form action="/add-stock" method="post">
            자재명: <input type="text" name="itemName" required> | 
            수량: <input type="number" name="quantity" required style="width: 50px;"> | 
            단위: <input type="text" name="unit" placeholder="EA, KG 등" required style="width: 60px;"> | 
            유통기한: <input type="date" name="expirationDate" required>
            <button type="submit" style="background-color: #ffcc00; font-weight: bold;">등록하기</button>
        </form>
    </div>

    <table border="1">
        <tr>
            <th>ID</th><th>자재명</th><th>수량</th><th>단위</th><th>유통기한</th>
        </tr>
        <c:forEach var="item" items="${stocks}">
            <tr>
                <td>${item.id}</td>
                <td>${item.itemName}</td>
                <td>
                    <form action="/update-stock" method="post" style="display:inline; margin:0; padding:0;">
                        <input type="hidden" name="id" value="${item.id}"> 
                        <input type="number" name="quantity" value="${item.quantity}" style="width:50px; text-align:center;">
                        <button type="submit" style="font-size:11px; padding:2px 5px;">변경</button>
                    </form>
                </td>
                <td>${item.unit}</td>
                <td>${item.expirationDate}</td>
                <td>
                    <a href="/delete-stock?id=${item.id}" onclick="return confirm('정말 삭제하시겠습니까?')">삭제</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>