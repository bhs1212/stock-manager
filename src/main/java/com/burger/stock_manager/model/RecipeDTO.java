package com.burger.stock_manager.model;

import lombok.Data;

@Data
public class RecipeDTO {
    private int id;
    private String menuName;
    private int stockId;
    private int requiredQuantity;
    private String itemName; // 화면 표시용 (stock 테이블의 자재명)
}
