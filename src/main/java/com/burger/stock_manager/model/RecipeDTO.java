package com.burger.stock_manager.model;

import lombok.Data;

@Data
public class RecipeDTO {
    private int stockId;
    private int requiredQuantity;
}
