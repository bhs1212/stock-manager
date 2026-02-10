package com.burger.stock_manager.controller;

import com.burger.stock_manager.mapper.StockMapper;
import com.burger.stock_manager.model.RecipeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class SalesController {

    @Autowired
    private StockMapper stockMapper;

    /**
     * 메뉴 판매 처리 (레시피 기반 재고 역산)
     */
    @PostMapping("/sell-menu")
    public String sellMenu(@RequestParam String menuName,
            @RequestParam int sellCount,
            RedirectAttributes rttr) {

        // 1. 해당 메뉴에 필요한 레시피 정보(재료 ID, 소모량)를 DB에서 가져옴
        List<RecipeDTO> recipes = stockMapper.getRecipeByMenu(menuName);

        // 2. 만약 등록된 레시피가 없다면 안내 메시지 후 리턴
        if (recipes == null || recipes.isEmpty()) {
            rttr.addFlashAttribute("error", menuName + "의 레시피 정보가 등록되지 않았습니다.");
            return "redirect:/inventory";
        }

        // 3. 레시피를 돌면서 각 재료의 재고를 차감
        for (RecipeDTO recipe : recipes) {
            // 소모 총량 = 메뉴 1개당 소모량 * 판매 개수
            int totalUsed = recipe.getRequiredQuantity() * sellCount;

            // StockMapper를 통해 DB의 quantity 업데이트
            stockMapper.decreaseStock(recipe.getStockId(), totalUsed);
        }

        // 4. 완료 메시지 전달 (일회성 데이터)
        rttr.addFlashAttribute("message", menuName + " " + sellCount + "개 판매가 완료되어 재고가 차감되었습니다.");

        return "redirect:/inventory";
    }
}
