package com.burger.stock_manager.service;

import com.burger.stock_manager.mapper.StockMapper;
import com.burger.stock_manager.model.RecipeDTO;
import com.burger.stock_manager.model.SalesLogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SalesService {

    @Autowired
    private StockMapper stockMapper;

    @Transactional
    public void processSale(String menuName, int sellCount) throws Exception {

        List<RecipeDTO> recipes = stockMapper.getRecipeByMenu(menuName);

        if (recipes == null || recipes.isEmpty()) {
            throw new Exception(menuName + "의 레시피 정보가 등록되지 않았습니다.");
        }

        for (RecipeDTO recipe : recipes) {
            int totalUsed = recipe.getRequiredQuantity() * sellCount;
            stockMapper.decreaseStock(recipe.getStockId(), totalUsed);
        }

        stockMapper.insertSalesLog(menuName, sellCount);
    }

    // 대시보드용 판매 로그 조회
    public List<SalesLogDTO> getSalesLogs() {

        return stockMapper.findAllSalesLogs();
    }
}
