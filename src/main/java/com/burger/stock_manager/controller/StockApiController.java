package com.burger.stock_manager.controller;

import com.burger.stock_manager.model.StockDTO;
import com.burger.stock_manager.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
public class StockApiController {

    private final StockService stockService;

    public StockApiController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getStocks(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page) {

        int size = 10;
        int offset = (page - 1) * size;

        List<StockDTO> stocks = stockService.getStocks(keyword, offset, size);
        int totalPages = stockService.getTotalPages(keyword, size);

        return ResponseEntity.ok(Map.of(
                "stocks", stocks,
                "currentPage", page,
                "totalPages", totalPages));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addStock(@RequestBody StockDTO stock) {
        stockService.addStock(stock);
        return ResponseEntity.ok(Map.of("message", "자재가 등록되었습니다."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateStock(
            @PathVariable int id,
            @RequestBody Map<String, Integer> body) {

        int quantity = body.get("quantity");
        if (quantity < 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "수량은 0 이상이어야 합니다."));
        }
        stockService.updateStockQuantity(id, quantity);
        return ResponseEntity.ok(Map.of("message", "수량이 변경되었습니다."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteStock(@PathVariable int id) {
        stockService.deleteStock(id);
        return ResponseEntity.ok(Map.of("message", "자재가 삭제되었습니다."));
    }
}