package com.burger.stock_manager.service;

import com.burger.stock_manager.exception.InsufficientStockException;
import com.burger.stock_manager.exception.RecipeNotFoundException;
import com.burger.stock_manager.mapper.SalesMapper;
import com.burger.stock_manager.mapper.StockMapper;
import com.burger.stock_manager.model.RecipeDTO;
import com.burger.stock_manager.model.SalesLogDTO;
import com.burger.stock_manager.model.SalesStatDTO;
import com.burger.stock_manager.model.StockDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SalesService {

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private SalesMapper salesMapper;

    @Transactional
    public void processSale(String menuName, int sellCount) {

        List<RecipeDTO> recipes = salesMapper.getRecipeByMenu(menuName);

        if (recipes == null || recipes.isEmpty()) {
            throw new RecipeNotFoundException(menuName);
        }

        // 차감 전 재고 충분한지 먼저 확인
        for (RecipeDTO recipe : recipes) {
            int totalUsed = recipe.getRequiredQuantity() * sellCount;
            StockDTO stock = stockMapper.findStockById(recipe.getStockId());

            if (stock == null || stock.getQuantity() < totalUsed) {
                throw new InsufficientStockException(stock != null ? stock.getItemName() : "알 수 없는 재료");
            }
        }

        // 검증 통과 후 차감
        for (RecipeDTO recipe : recipes) {
            int totalUsed = recipe.getRequiredQuantity() * sellCount;
            stockMapper.decreaseStock(recipe.getStockId(), totalUsed);
        }

        salesMapper.insertSalesLog(menuName, sellCount);
    }

    // 대시보드용 판매 로그 조회
    public List<SalesLogDTO> getSalesLogs() {

        return salesMapper.findAllSalesLogs();
    }

    public List<SalesStatDTO> getDailyStats() {
        return salesMapper.getDailyStats();
    }

    public List<SalesStatDTO> getWeeklyStats() {
        return salesMapper.getWeeklyStats();
    }

    public List<SalesStatDTO> getMonthlyStats() {
        return salesMapper.getMonthlyStats();
    }

    public List<SalesStatDTO> getStatsByMonth(int year, int month) {
        return salesMapper.getStatsByMonth(year, month);
    }
}
