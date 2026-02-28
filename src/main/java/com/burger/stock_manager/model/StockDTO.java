package com.burger.stock_manager.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class StockDTO {
    private int id;
    private String itemName;
    private int quantity;
    private String unit;
    private LocalDate expirationDate;

    // 계산 로직은 지우고 계산된 결과값을 담을 변수만 남겨두기
    private long daysUntilExpiration;
}