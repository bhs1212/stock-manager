package com.burger.stock_manager.model;

import lombok.Data;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

@Data // Getter, Setter 등을 자동으로 만들어줍니다
public class StockDTO {
    private int id;
    private String itemName;
    private int quantity;
    private String unit;

    //이 어노테이션을 추가해서 날짜 형식을 알려줍니다.
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;
}
