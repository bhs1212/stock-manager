package com.burger.stock_manager.model;

import lombok.Data;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.format.annotation.DateTimeFormat;

@Data // Getter, Setter 등을 자동으로 만들어줍니다
public class StockDTO {
    private int id;
    private String itemName;
    private int quantity;
    private String unit;

    // 이 어노테이션을 추가해서 날짜 형식을 알려줍니다.
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;

    // 유통기한까지 남은 일수를 계산하는 메서드 추가
    public long getDaysUntilExpiration() {
        if (this.expirationDate == null)
            return 999; // 날짜가 없으면 안전하게 큰 값 반환

        // 현재 날짜 (0시 0분 0초 기준)
        Date now = new Date();

        // 두 날짜의 차이 계산 (밀리초 단위)
        long diffInMillies = this.expirationDate.getTime() - now.getTime();

        // 밀리초를 일(day) 단위로 변환 (소수점 버림)
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
