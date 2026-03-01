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
    private long daysUntilExpiration;
}