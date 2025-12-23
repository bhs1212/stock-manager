package com.burger.stock_manager.mapper;

import com.burger.stock_manager.model.StockDTO;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface StockMapper {
    @Select("SELECT id, item_name as itemName, quantity, unit, expiration_date as expirationDate FROM stock")
    List<StockDTO> findAll();

    // StockMapper.java 인터페이스 안에 추가
    @Insert("INSERT INTO stock (item_name, quantity, unit, expiration_date) " +
            "VALUES (#{itemName}, #{quantity}, #{unit}, #{expirationDate})")
    void insertStock(StockDTO stock);

    @org.apache.ibatis.annotations.Delete("DELETE FROM stock WHERE id = #{id}")
    void deleteStock(int id);

    @org.apache.ibatis.annotations.Update("UPDATE stock SET quantity = #{quantity} WHERE id = #{id}")
    void updateQuantity(int id, int quantity);
}
