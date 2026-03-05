package com.burger.stock_manager.controller;

import com.burger.stock_manager.model.RecipeDTO;
import com.burger.stock_manager.service.StockService;
import com.burger.stock_manager.service.SalesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

@Controller
public class SalesController {

    private final SalesService salesService;
    private final StockService stockService;

    public SalesController(SalesService salesService, StockService stockService) {
        this.salesService = salesService;
        this.stockService = stockService;
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

        ObjectMapper mapper = new ObjectMapper();
        try {
            model.addAttribute("dailyStatsJson", mapper.writeValueAsString(salesService.getDailyStats()));
            model.addAttribute("weeklyStatsJson", mapper.writeValueAsString(salesService.getWeeklyStats()));
            model.addAttribute("monthlyStatsJson", mapper.writeValueAsString(salesService.getMonthlyStats()));
            model.addAttribute("customStatsJson", mapper.writeValueAsString(salesService.getStatsByMonth(year, month)));
        } catch (JsonProcessingException e) {
            model.addAttribute("dailyStatsJson", "[]");
            model.addAttribute("weeklyStatsJson", "[]");
            model.addAttribute("monthlyStatsJson", "[]");
            model.addAttribute("customStatsJson", "[]");
        }

        return "sales-dashboard";
    }

    @GetMapping("/recipe")
    public String recipePage(Model model) {
        model.addAttribute("recipes", salesService.getAllRecipes());
        model.addAttribute("stocks", stockService.getStocks(null, 0, 1000));
        return "recipe";
    }

    @PostMapping("/add-recipe")
    public String addRecipe(RecipeDTO recipe, RedirectAttributes rttr) {
        salesService.addRecipe(recipe);
        rttr.addFlashAttribute("message", "레시피가 등록되었습니다.");
        return "redirect:/recipe";
    }

    @PostMapping("/delete-recipe")
    public String deleteRecipe(@RequestParam int id, RedirectAttributes rttr) {
        salesService.deleteRecipe(id);
        rttr.addFlashAttribute("message", "레시피가 삭제되었습니다.");
        return "redirect:/recipe";
    }
}