package com.burger.stock_manager.service;

import com.burger.stock_manager.exception.InsufficientStockException;
import com.burger.stock_manager.exception.RecipeNotFoundException;
import com.burger.stock_manager.mapper.SalesMapper;
import com.burger.stock_manager.mapper.StockMapper;
import com.burger.stock_manager.model.RecipeDTO;
import com.burger.stock_manager.model.SalesLogDTO;
import com.burger.stock_manager.model.SalesStatDTO;
import com.burger.stock_manager.model.StockDTO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SalesService {

    private final StockMapper stockMapper;
    private final SalesMapper salesMapper;

    public SalesService(StockMapper stockMapper, SalesMapper salesMapper) {
        this.stockMapper = stockMapper;
        this.salesMapper = salesMapper;
    }

    @Transactional
    public void processSale(String menuName, int sellCount) {

        List<RecipeDTO> recipes = salesMapper.getRecipeByMenu(menuName);

        if (recipes == null || recipes.isEmpty()) {
            throw new RecipeNotFoundException(menuName);
        }

        // 검증하면서 차감량을 미리 계산해두기
        Map<Integer, Integer> deductionMap = new HashMap<>();

        for (RecipeDTO recipe : recipes) {
            int totalUsed = recipe.getRequiredQuantity() * sellCount;
            StockDTO stock = stockMapper.findStockById(recipe.getStockId());

            if (stock == null || stock.getQuantity() < totalUsed) {
                throw new InsufficientStockException(stock != null ? stock.getItemName() : "알 수 없는 재료");
            }
            deductionMap.put(recipe.getStockId(), totalUsed);
        }

        // 검증 통과 후 차감 (추가 DB 조회 없음)
        for (Map.Entry<Integer, Integer> entry : deductionMap.entrySet()) {
            stockMapper.decreaseStock(entry.getKey(), entry.getValue());
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

    public List<RecipeDTO> getAllRecipes() {
        return salesMapper.findAllRecipes();
    }

    public void addRecipe(RecipeDTO recipe) {
        salesMapper.insertRecipe(recipe);
    }

    public void deleteRecipe(int id) {
        salesMapper.deleteRecipe(id);
    }

    public List<SalesStatDTO> getMenuList() {
        return salesMapper.getMenuList();
    }

    public List<SalesLogDTO> getSalesLogs(int offset, int size) {
        return salesMapper.findSalesLogsWithPaging(offset, size);
    }

    public int getSalesLogTotalPages(int size) {
        int totalCount = salesMapper.countSalesLogs();
        return (int) Math.ceil((double) totalCount / size);
    }
}
