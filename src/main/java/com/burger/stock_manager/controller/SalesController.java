package com.burger.stock_manager.controller;

import com.burger.stock_manager.service.SalesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class SalesController {

    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @PostMapping("/sell-menu")
    public String sellMenu(@RequestParam String menuName,
            @RequestParam int sellCount,
            RedirectAttributes rttr) {

        salesService.processSale(menuName, sellCount);
        rttr.addFlashAttribute("message", menuName + " " + sellCount + "개 판매 및 기록 완료");

        return "redirect:/inventory";
    }

    @GetMapping("/sales-dashboard")
    public String salesDashboard(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month,
            Model model) {

        // 기본값: 현재 연월
        LocalDate now = LocalDate.now();
        if (year == null)
            year = now.getYear();
        if (month == null)
            month = now.getMonthValue();

        model.addAttribute("salesLogs", salesService.getSalesLogs());
        model.addAttribute("dailyStats", salesService.getDailyStats());
        model.addAttribute("weeklyStats", salesService.getWeeklyStats());
        model.addAttribute("monthlyStats", salesService.getMonthlyStats());
        model.addAttribute("customStats", salesService.getStatsByMonth(year, month));
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedMonth", month);

        return "sales-dashboard";
    }
}