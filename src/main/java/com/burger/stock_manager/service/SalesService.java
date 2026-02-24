package com.burger.stock_manager.service;

import com.burger.stock_manager.mapper.StockMapper;
import com.burger.stock_manager.model.RecipeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SalesService {

    @Autowired
    private StockMapper stockMapper;

    // @Transactional이 붙어있어서, 중간에 에러가 나면 모든 작업(재고 차감 등)이 취소되고 원래대로 돌아갑니다.
    @Transactional
    public void processSale(String menuName, int sellCount) throws Exception {

        // 해당 메뉴에 필요한 레시피 정보 가져오기
        List<RecipeDTO> recipes = stockMapper.getRecipeByMenu(menuName);

        // 레시피가 없으면 예외 발생 (컨트롤러가 이 메시지를 받아서 화면에 띄워줍니다)
        if (recipes == null || recipes.isEmpty()) {
            throw new Exception(menuName + "의 레시피 정보가 등록되지 않았습니다.");
        }

        // 레시피를 돌면서 각 재료의 재고 차감
        for (RecipeDTO recipe : recipes) {
            int totalUsed = recipe.getRequiredQuantity() * sellCount;
            stockMapper.decreaseStock(recipe.getStockId(), totalUsed);
        }

        // 판매 내역 기록
        stockMapper.insertSalesLog(menuName, sellCount);
    }
}
