package com.burger.stock_manager.service;

import com.burger.stock_manager.mapper.StockMapper;
import com.burger.stock_manager.model.StockDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockMapper stockMapper;

    @InjectMocks
    private StockService stockService;

    @Test
    @DisplayName("재고 목록 조회 시 유통기한 남은 일수가 계산된다")
    void getStocks_calculatesDaysUntilExpiration() {
        StockDTO stock = new StockDTO();
        stock.setId(1);
        stock.setItemName("패티");
        stock.setQuantity(50);
        stock.setExpirationDate(LocalDate.now().plusDays(5));

        when(stockMapper.findAll(null, 0, 10)).thenReturn(List.of(stock));

        List<StockDTO> result = stockService.getStocks(null, 0, 10);

        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getDaysUntilExpiration());
    }

    @Test
    @DisplayName("유통기한 지난 재고는 음수 일수로 계산된다")
    void getStocks_expiredStock() {
        StockDTO stock = new StockDTO();
        stock.setId(1);
        stock.setItemName("양상추");
        stock.setQuantity(10);
        stock.setExpirationDate(LocalDate.now().minusDays(3));

        when(stockMapper.findAll(null, 0, 10)).thenReturn(List.of(stock));

        List<StockDTO> result = stockService.getStocks(null, 0, 10);

        assertEquals(-3, result.get(0).getDaysUntilExpiration());
    }

    @Test
    @DisplayName("자재명이 비어있으면 예외 발생")
    void addStock_emptyName_throwsException() {
        StockDTO stock = new StockDTO();
        stock.setItemName("");
        stock.setQuantity(10);

        assertThrows(IllegalArgumentException.class, () -> {
            stockService.addStock(stock);
        });

        verify(stockMapper, never()).insertStock(any());
    }

    @Test
    @DisplayName("수량이 음수이면 예외 발생")
    void addStock_negativeQuantity_throwsException() {
        StockDTO stock = new StockDTO();
        stock.setItemName("패티");
        stock.setQuantity(-1);

        assertThrows(IllegalArgumentException.class, () -> {
            stockService.addStock(stock);
        });

        verify(stockMapper, never()).insertStock(any());
    }

    @Test
    @DisplayName("삭제된 자재 재등록 시 복원된다")
    void addStock_deletedItem_restores() {
        StockDTO newStock = new StockDTO();
        newStock.setItemName("패티");
        newStock.setQuantity(30);
        newStock.setUnit("EA");
        newStock.setExpirationDate(LocalDate.now().plusDays(7));

        StockDTO existing = new StockDTO();
        existing.setId(5);
        existing.setItemName("패티");

        when(stockMapper.findByNameIncludeDeleted("패티")).thenReturn(existing);

        stockService.addStock(newStock);

        verify(stockMapper).restoreStock(newStock);
        verify(stockMapper, never()).insertStock(any());
        assertEquals(5, newStock.getId());
    }

    @Test
    @DisplayName("신규 자재 등록")
    void addStock_newItem_inserts() {
        StockDTO stock = new StockDTO();
        stock.setItemName("새우패티");
        stock.setQuantity(20);
        stock.setUnit("EA");

        when(stockMapper.findByNameIncludeDeleted("새우패티")).thenReturn(null);

        stockService.addStock(stock);

        verify(stockMapper).insertStock(stock);
        verify(stockMapper, never()).restoreStock(any());
    }

    @Test
    @DisplayName("전체 페이지 수 계산")
    void getTotalPages_calculatesCorrectly() {
        when(stockMapper.countTotal(null)).thenReturn(25);

        int totalPages = stockService.getTotalPages(null, 10);

        assertEquals(3, totalPages);
    }
}
