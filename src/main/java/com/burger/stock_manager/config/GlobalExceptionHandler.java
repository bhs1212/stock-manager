package com.burger.stock_manager.config;

import com.burger.stock_manager.exception.InsufficientStockException;
import com.burger.stock_manager.exception.RecipeNotFoundException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ InsufficientStockException.class, RecipeNotFoundException.class })
    public String handleSalesException(RuntimeException e, RedirectAttributes rttr) {
        rttr.addFlashAttribute("error", e.getMessage());
        return "redirect:/inventory";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException e, RedirectAttributes rttr) {
        rttr.addFlashAttribute("error", e.getMessage());
        return "redirect:/inventory";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationException(MethodArgumentNotValidException e, RedirectAttributes rttr) {
        rttr.addFlashAttribute("error", "입력값을 확인해주세요.");
        return "redirect:/inventory";
    }
}
