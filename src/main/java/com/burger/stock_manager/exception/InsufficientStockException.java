package com.burger.stock_manager.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String itemName) {
        super(itemName + " 재고가 부족합니다.");
    }
}