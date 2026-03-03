package com.burger.stock_manager.service;

import com.burger.stock_manager.exception.InsufficientStockException;
import com.burger.stock_manager.exception.RecipeNotFoundException;
import com.burger.stock_manager.mapper.SalesMapper;
import com.burger.stock_manager.mapper.StockMapper;
import com.burger.stock_manager.model.RecipeDTO;
import com.burger.stock_manager.model.StockDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalesServiceTest {

    @Mock
    private StockMapper stockMapper;

    @Mock
    private SalesMapper salesMapper;

    @InjectMocks
    private SalesService salesService;

    @Test
    @DisplayName("정상 판매 - 재고 차감 및 판매 로그 저장")
    void processSale_success() {
        // given
        RecipeDTO recipe = new RecipeDTO();
        recipe.setStockId(1);
        recipe.setRequiredQuantity(2);

        StockDTO stock = new StockDTO();
        stock.setId(1);
        stock.setItemName("패티");
        stock.setQuantity(100);

        when(salesMapper.getRecipeByMenu("와퍼")).thenReturn(List.of(recipe));
        when(stockMapper.findStockById(1)).thenReturn(stock);

        // when
        salesService.processSale("와퍼", 3);

        // then
        verify(stockMapper).decreaseStock(1, 6);
        verify(salesMapper).insertSalesLog("와퍼", 3);
    }

    @Test
    @DisplayName("레시피 없는 메뉴 판매 시 예외 발생")
    void processSale_recipeNotFound() {
        when(salesMapper.getRecipeByMenu("없는메뉴")).thenReturn(Collections.emptyList());

        assertThrows(RecipeNotFoundException.class, () -> {
            salesService.processSale("없는메뉴", 1);
        });
    }

    @Test
    @DisplayName("재고 부족 시 예외 발생")
    void processSale_insufficientStock() {
        RecipeDTO recipe = new RecipeDTO();
        recipe.setStockId(1);
        recipe.setRequiredQuantity(10);

        StockDTO stock = new StockDTO();
        stock.setId(1);
        stock.setItemName("패티");
        stock.setQuantity(5);

        when(salesMapper.getRecipeByMenu("와퍼")).thenReturn(List.of(recipe));
        when(stockMapper.findStockById(1)).thenReturn(stock);

        assertThrows(InsufficientStockException.class, () -> {
            salesService.processSale("와퍼", 1);
        });

        verify(stockMapper, never()).decreaseStock(anyInt(), anyInt());
    }

    @Test
    @DisplayName("판매 수량이 여러 개일 때 재고 차감량 계산 확인")
    void processSale_multipleCount() {
        RecipeDTO recipe1 = new RecipeDTO();
        recipe1.setStockId(1);
        recipe1.setRequiredQuantity(2);

        RecipeDTO recipe2 = new RecipeDTO();
        recipe2.setStockId(2);
        recipe2.setRequiredQuantity(1);

        StockDTO stock1 = new StockDTO();
        stock1.setId(1);
        stock1.setItemName("패티");
        stock1.setQuantity(50);

        StockDTO stock2 = new StockDTO();
        stock2.setId(2);
        stock2.setItemName("번");
        stock2.setQuantity(30);

        when(salesMapper.getRecipeByMenu("치즈버거")).thenReturn(List.of(recipe1, recipe2));
        when(stockMapper.findStockById(1)).thenReturn(stock1);
        when(stockMapper.findStockById(2)).thenReturn(stock2);

        salesService.processSale("치즈버거", 5);

        verify(stockMapper).decreaseStock(1, 10);
        verify(stockMapper).decreaseStock(2, 5);
        verify(salesMapper).insertSalesLog("치즈버거", 5);
    }
}