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

    // 새로운 재고 목록 페이지 (http://localhost:8080/inventory)
    @GetMapping("/inventory")
    public String inventoryPage(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page, // 페이지 번호 추가 (기본값 1)
            HttpSession session,
            Model model) {

        // 1. 로그인 체크 (기존과 동일)
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        // 2. 페이징 설정
        int size = 10; // 한 페이지에 보여줄 개수
        int offset = (page - 1) * size; // 시작 지점 계산 (1페이지면 0부터, 2페이지면 10부터)

        // 3. 데이터 가져오기 (수정한 Mapper 메서드 호출)
        List<StockDTO> stocks = stockMapper.findAll(keyword, offset, size);

        // 4. 전체 페이지 수 계산
        int totalCount = stockMapper.countTotal(keyword); // 전체 게시글 수
        int totalPages = (int) Math.ceil((double) totalCount / size); // 총 페이지 수 계산

        // 5. 화면(JSP)으로 전달
        model.addAttribute("stocks", stocks);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "inventory";
    }

    @PostMapping("/add-stock")
    public String addStock(StockDTO stock, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");

        // 관리자가 아니면 등록 불가
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/inventory";
        }

        stockMapper.insertStock(stock);
        return "redirect:/inventory";
    }

    @GetMapping("/delete-stock")
    public String deleteStock(int id, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");

        // 관리자가 아니면 삭제 불가
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/inventory";
        }

        stockMapper.deleteStock(id);
        return "redirect:/inventory";
    }

    @PostMapping("/update-stock")
    public String updateStock(int id, int quantity, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");

        // 관리자가 아니면 수정 불가
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/inventory";
        }

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

    // 1. 회원가입 페이지 이동
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // 2. 회원가입 처리
    @PostMapping("/register")
    public String register(UserDTO user, Model model) {

        // 1. 아이디 중복 확인
        int count = userMapper.existsByUsername(user.getUsername());
        if (count > 0) {
            // 이미 아이디가 존재한다면 에러 메시지와 함께 다시 가입 페이지로!
            model.addAttribute("error", "이미 존재하는 아이디입니다.");
            return "register";
        }

        // 2. 중복이 아니라면 기존처럼 암호화 및 저장 진행
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userMapper.insertUser(user);

        return "redirect:/login";
    }
}