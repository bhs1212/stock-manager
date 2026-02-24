package com.burger.stock_manager.interceptor;

import com.burger.stock_manager.model.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

@Component
public class RoleCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        // 아예 로그인을 안 한 경우 -> 로그인 페이지로 조용히 보냅니다.
        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }

        // 로그인은 했지만, 관리자(admin)가 아닌 경우 알림창을 띄웁니다
        if (!"admin".equals(user.getRole())) {
            // 화면에 HTML과 자바스크립트를 직접 출력하는 설정
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();

            // 경고창을 띄우고 이전 페이지로 돌아가게 만듭니다.
            out.println("<script>alert('권한이 없습니다.'); history.back();</script>");
            out.flush();

            return false; // 컨트롤러로 더 이상 넘어가지 못하게 막음
        }

        // admin인 경우 통과
        return true;
    }
}