package com.burger.stock_manager.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class SalesLogDTO {
    private int id;
    private String menuName;
    private int sellCount;
    private Timestamp saleDate;

}
