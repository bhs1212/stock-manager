package com.burger.stock_manager.controller;

import com.burger.stock_manager.model.SalesLogDTO;
import com.burger.stock_manager.service.SalesService; // 서비스 임포트
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class SalesController {

    @Autowired
    private SalesService salesService;

    @PostMapping("/sell-menu")
    public String sellMenu(@RequestParam String menuName,
            @RequestParam int sellCount,
            RedirectAttributes rttr) {

        salesService.processSale(menuName, sellCount);
        rttr.addFlashAttribute("message", menuName + " " + sellCount + "개 판매 및 기록 완료");

        return "redirect:/inventory";
    }

    @GetMapping("/sales-dashboard")
    public String salesDashboard(Model model) {
        List<SalesLogDTO> salesLogs = salesService.getSalesLogs();

        model.addAttribute("salesLogs", salesLogs);
        return "sales-dashboard";
    }
}