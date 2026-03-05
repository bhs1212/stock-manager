package com.burger.stock_manager.controller;

import com.burger.stock_manager.model.StockDTO;
import com.burger.stock_manager.service.SalesService;
import com.burger.stock_manager.service.StockService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Validated
public class StockController {

    private final StockService stockService;
    private final SalesService salesService;

    public StockController(StockService stockService, SalesService salesService) {
        this.stockService = stockService;
        this.salesService = salesService;
    }

    // 새로운 재고 목록 페이지
    @GetMapping("/inventory")
    public String inventoryPage(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {

        int size = 10;
        int offset = (page - 1) * size;

        List<StockDTO> stocks = stockService.getStocks(keyword, offset, size);
        int totalPages = stockService.getTotalPages(keyword, size);

        model.addAttribute("stocks", stocks);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("menuList", salesService.getMenuList());

        return "inventory";
    }

    @PostMapping("/add-stock")
    public String addStock(StockDTO stock) {
        stockService.addStock(stock);
        return "redirect:/inventory";
    }

    @PostMapping("/delete-stock")
    public String deleteStock(@RequestParam int id) {
        stockService.deleteStock(id);
        return "redirect:/inventory";
    }

    @PostMapping("/update-stock")
    public String updateStock(@RequestParam int id, @RequestParam int quantity) {
        if (quantity < 0) {
            return "redirect:/inventory";
        }
        stockService.updateStockQuantity(id, quantity);
        return "redirect:/inventory";
    }
}