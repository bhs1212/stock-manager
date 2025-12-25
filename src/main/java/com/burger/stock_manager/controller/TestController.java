package com.burger.stock_manager.controller;

import com.burger.stock_manager.mapper.StockMapper;
import com.burger.stock_manager.model.StockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.burger.stock_manager.mapper.UserMapper;
import com.burger.stock_manager.model.UserDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Controller
public class TestController {

    // 1. Mapper 연결 (자동 주입)
    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private UserMapper userMapper; // 주입 추가

    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping("/test")
    public String testPage(Model model) {
        // 화면에 전달할 데이터 담기
        model.addAttribute("msg", "재고관리 시스템에 오신 것을 환영합니다!");

        // return하는 문자열이 JSP 파일의 이름이 됩니다.
        // 설정에 의해 /WEB-INF/views/hello.jsp 를 찾아가게 됩니다.
        return "hello";
    }

    // 2. 새로운 재고 목록 페이지 (http://localhost:8080/inventory)
    @GetMapping("/inventory")
    public String inventoryPage(@RequestParam(value = "keyword", required = false) String keyword,
            HttpSession session, // 1. 세션을 인자로 받습니다.
            Model model) {

        // 2. 세션에 "user" 정보가 있는지 확인합니다.
        if (session.getAttribute("user") == null) {
            // 로그인 정보가 없으면 로그인 페이지로 강제로 보냅니다.
            return "redirect:/login";
        }

        // 기존 로직 수행
        List<StockDTO> stocks = stockMapper.findAll(keyword);
        model.addAttribute("stocks", stocks);
        model.addAttribute("keyword", keyword);
        return "inventory";
    }

    @PostMapping("/add-stock")
    public String addStock(StockDTO stock) {
        // 1. 화면에서 보낸 데이터를 DB에 저장함
        stockMapper.insertStock(stock);

        // 2. 저장이 끝나면 다시 목록 페이지(/inventory)로 강제 이동함
        return "redirect:/inventory";
    }

    @GetMapping("/delete-stock")
    public String deleteStock(int id) {
        stockMapper.deleteStock(id);
        return "redirect:/inventory";
    }

    @PostMapping("/update-stock")
    public String updateStock(int id, int quantity) {
        stockMapper.updateQuantity(id, quantity);
        return "redirect:/inventory";
    }

    // 로그인 페이지 이동
    @GetMapping("/login")
    public String loginPage() {

        return "login";
    }

    // 로그인 기능 처리
    @PostMapping("/login")
    public String login(String username, String password, HttpSession session, Model model) {
        // 암호화 로그인을 위해 아이디로 먼저 사용자를 찾습니다.
        UserDTO user = userMapper.findByUsername(username);

        // encoder.matches를 이용해 입력한 비번과 DB의 암호화된 비번을 비교합니다.
        if (user != null && encoder.matches(password, user.getPassword())) {
            // 로그인 성공: 세션에 사용자 정보 저장 (중요!)
            session.setAttribute("user", user);
            return "redirect:/inventory"; // 재고 목록으로 이동
        } else {
            // 로그인 실패
            model.addAttribute("error", "아이디 또는 비밀번호가 틀렸습니다.");
            return "login";
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 모든 세션 정보 삭제
        return "redirect:/login";
    }
}