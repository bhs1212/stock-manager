package com.burger.stock_manager.interceptor;

import com.burger.stock_manager.model.UserDTO;
import com.burger.stock_manager.model.UserSessionDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

@Component
public class AdminCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");

        if (user == null || !"admin".equals(user.getRole())) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('관리자 권한이 없습니다.'); history.back();</script>");
            out.flush();
            return false;
        }

        return true;
    }
}