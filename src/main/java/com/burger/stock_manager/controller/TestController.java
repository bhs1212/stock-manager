package com.burger.stock_manager.controller;

import com.burger.stock_manager.mapper.StockMapper;
import com.burger.stock_manager.model.StockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TestController {

    // 1. Mapper 연결 (자동 주입)
    @Autowired
    private StockMapper stockMapper;

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
    public String inventoryPage(Model model) {
        // DB에서 재고 목록을 싹 가져옵니다.
        List<StockDTO> stocks = stockMapper.findAll();
        
        // 가져온 리스트를 "stocks"라는 이름으로 화면(JSP)에 보냅니다.
        model.addAttribute("stocks", stocks);
        
        // WEB-INF/views/inventory.jsp 파일을 찾아가라!
        return "inventory";
    }

    @PostMapping("/add-stock")
    public String addStock(StockDTO stock) {
        // 1. 화면에서 보낸 데이터를 DB에 저장함
        stockMapper.insertStock(stock);
        
        // 2. 저장이 끝나면 다시 목록 페이지(/inventory)로 강제 이동함
        return "redirect:/inventory";
    }

    @org.springframework.web.bind.annotation.GetMapping("/delete-stock")
    public String deleteStock(int id) {
        stockMapper.deleteStock(id);
        return "redirect:/inventory";
    }
    @PostMapping("/update-stock")
    public String updateStock(int id, int quantity) {
        stockMapper.updateQuantity(id, quantity);
        return "redirect:/inventory";
    }
}
