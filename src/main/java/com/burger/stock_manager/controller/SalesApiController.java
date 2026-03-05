package com.burger.stock_manager.controller;

import com.burger.stock_manager.exception.InsufficientStockException;
import com.burger.stock_manager.exception.RecipeNotFoundException;
import com.burger.stock_manager.model.RecipeDTO;
import com.burger.stock_manager.model.SalesLogDTO;
import com.burger.stock_manager.model.SalesStatDTO;
import com.burger.stock_manager.service.SalesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales")
public class SalesApiController {

    private final SalesService salesService;

    public SalesApiController(SalesService salesService) {
        this.salesService = salesService;
    }

    @PostMapping("/sell")
    public ResponseEntity<Map<String, String>> sellMenu(@RequestBody Map<String, Object> body) {
        String menuName = (String) body.get("menuName");
        int sellCount = (int) body.get("sellCount");

        try {
            salesService.processSale(menuName, sellCount);
            return ResponseEntity.ok(Map.of("message", menuName + " " + sellCount + "개 판매 완료"));
        } catch (RecipeNotFoundException | InsufficientStockException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/logs")
    public ResponseEntity<List<SalesLogDTO>> getSalesLogs() {
        return ResponseEntity.ok(salesService.getSalesLogs());
    }

    @GetMapping("/stats/daily")
    public ResponseEntity<List<SalesStatDTO>> getDailyStats() {
        return ResponseEntity.ok(salesService.getDailyStats());
    }

    @GetMapping("/stats/weekly")
    public ResponseEntity<List<SalesStatDTO>> getWeeklyStats() {
        return ResponseEntity.ok(salesService.getWeeklyStats());
    }

    @GetMapping("/stats/monthly")
    public ResponseEntity<List<SalesStatDTO>> getMonthlyStats() {
        return ResponseEntity.ok(salesService.getMonthlyStats());
    }

    @GetMapping("/stats/{year}/{month}")
    public ResponseEntity<List<SalesStatDTO>> getStatsByMonth(
            @PathVariable int year,
            @PathVariable int month) {
        return ResponseEntity.ok(salesService.getStatsByMonth(year, month));
    }

    @GetMapping("/menus")
    public ResponseEntity<List<SalesStatDTO>> getMenuList() {
        return ResponseEntity.ok(salesService.getMenuList());
    }

    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeDTO>> getRecipes() {
        return ResponseEntity.ok(salesService.getAllRecipes());
    }

    @PostMapping("/recipes")
    public ResponseEntity<Map<String, String>> addRecipe(@RequestBody RecipeDTO recipe) {
        salesService.addRecipe(recipe);
        return ResponseEntity.ok(Map.of("message", "레시피가 등록되었습니다."));
    }

    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<Map<String, String>> deleteRecipe(@PathVariable int id) {
        salesService.deleteRecipe(id);
        return ResponseEntity.ok(Map.of("message", "레시피가 삭제되었습니다."));
    }
}