package com.burger.stock_manager.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class SalesLogDTO {
    private int id;
    private String menuName;
    private int sellCount;
    private Timestamp saleDate;

    // Getter, Setter 생략 (직접 생성하시거나 @Data 사용)
}
