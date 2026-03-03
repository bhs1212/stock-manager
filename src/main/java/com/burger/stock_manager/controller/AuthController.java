package com.burger.stock_manager.controller;

import com.burger.stock_manager.model.UserDTO;
import com.burger.stock_manager.model.UserSessionDTO;
import com.burger.stock_manager.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // 로그인 페이지 이동
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(String username, String password, HttpSession session, Model model) {

        UserSessionDTO user = userService.authenticate(username, password);

        if (user != null) {
            session.setAttribute("user", user); // 인증 성공 시 세션 발급
            return "redirect:/inventory";
        } else {
            model.addAttribute("error", "아이디 또는 비밀번호가 틀렸습니다.");
            return "login";
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // 회원가입 페이지 이동
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // 회원가입 처리
    @PostMapping("/register")
    public String register(UserDTO user, Model model) {

        if (userService.isUsernameTaken(user.getUsername())) {
            model.addAttribute("error", "이미 존재하는 아이디입니다.");
            return "register";
        }

        userService.registerUser(user);

        return "redirect:/login";
    }
}