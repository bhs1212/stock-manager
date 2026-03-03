package com.burger.stock_manager.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SalesLogDTO {
    private int id;
    private String menuName;
    private int sellCount;
    private LocalDateTime saleDate;
}
