package com.burger.stock_manager.controller;

import com.burger.stock_manager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.burger.stock_manager.model.UserDTO;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 틀렸습니다.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

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