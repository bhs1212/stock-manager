package com.burger.stock_manager.mapper;

import com.burger.stock_manager.model.StockDTO;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface StockMapper {
    //@Select 쿼리를 <script>를 사용하는 동적 쿼리로 변경 (keyword 처리)
    @Select("<script>" +
            "SELECT id, item_name as itemName, quantity, unit, expiration_date as expirationDate FROM stock " +
            "WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND item_name LIKE CONCAT('%', #{keyword}, '%') " +
            "</if>" +
            "</script>")
    // 메서드 괄호 안에 String keyword를 반드시 넣어줘야함
    List<StockDTO> findAll(@org.apache.ibatis.annotations.Param("keyword") String keyword);

    @Insert("INSERT INTO stock (item_name, quantity, unit, expiration_date) " +
            "VALUES (#{itemName}, #{quantity}, #{unit}, #{expirationDate})")
    void insertStock(StockDTO stock);

    @org.apache.ibatis.annotations.Delete("DELETE FROM stock WHERE id = #{id}")
    void deleteStock(int id);

    @org.apache.ibatis.annotations.Update("UPDATE stock SET quantity = #{quantity} WHERE id = #{id}")
    void updateQuantity(@org.apache.ibatis.annotations.Param("id") int id, 
                        @org.apache.ibatis.annotations.Param("quantity") int quantity);
}
