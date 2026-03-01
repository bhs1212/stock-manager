package com.burger.stock_manager.service;

import com.burger.stock_manager.mapper.StockMapper;
import com.burger.stock_manager.model.StockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockMapper stockMapper;

    // 재고 목록 데이터 가져오기, 유통기한 계산하기
    public List<StockDTO> getStocks(String keyword, int offset, int size) {
        List<StockDTO> stocks = stockMapper.findAll(keyword, offset, size);

        LocalDate today = LocalDate.now();
        for (StockDTO stock : stocks) {
            if (stock.getExpirationDate() != null) {
                long daysBetween = ChronoUnit.DAYS.between(today, stock.getExpirationDate());
                stock.setDaysUntilExpiration(daysBetween);
            }
        }

        return stocks;
    }

    // 전체 페이지 수 계산
    public int getTotalPages(String keyword, int size) {
        int totalCount = stockMapper.countTotal(keyword);
        return (int) Math.ceil((double) totalCount / size);
    }

    // 재고 추가
    @Transactional
    public void addStock(StockDTO stock) {
        if (stock.getItemName() == null || stock.getItemName().isBlank()) {
            throw new IllegalArgumentException("자재명은 필수입니다.");
        }
        if (stock.getQuantity() < 0) { // null 체크 제거, int는 null이 없음
            throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");
        }

        StockDTO existingStock = stockMapper.findByNameIncludeDeleted(stock.getItemName());
        if (existingStock != null) {
            stock.setId(existingStock.getId());
            stockMapper.restoreStock(stock);
        } else {
            stockMapper.insertStock(stock);
        }
    }

    // 재고 삭제
    public void deleteStock(int id) {
        stockMapper.deleteStock(id);
    }

    // 재고 수량 수정
    public void updateStockQuantity(int id, int quantity) {
        stockMapper.updateQuantity(id, quantity);
    }
}