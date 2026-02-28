package com.burger.stock_manager.exception;

public class RecipeNotFoundException extends RuntimeException {
    public RecipeNotFoundException(String menuName) {
        super(menuName + "의 레시피 정보가 등록되지 않았습니다.");
    }
}