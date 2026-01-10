package com.burger.stock_manager.mapper;

import com.burger.stock_manager.model.StockDTO;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface StockMapper {
        // @Select 쿼리를 <script>를 사용하는 동적 쿼리로 변경 (keyword 처리)
        @Select("<script>" +
                        "SELECT id, item_name as itemName, quantity, unit, expiration_date as expirationDate FROM stock "
                        +
                        "WHERE 1=1 " +
                        "<if test='keyword != null and keyword != \"\"'>" +
                        "  AND item_name LIKE CONCAT('%', #{keyword}, '%') " +
                        "</if>" +
                        "ORDER BY expirationDate ASC " + // 유통기한 임박순 정렬 추가
                        "LIMIT #{offset}, #{size}" + // 페이징 처리
                        "</script>")
        List<StockDTO> findAll(@Param("keyword") String keyword,
                        @Param("offset") int offset,
                        @Param("size") int size);

        // 전체 페이지 번호를 계산하기 위해 '총 개수'를 가져오는 쿼리 추가
        @Select("<script>" +
                        "SELECT COUNT(*) FROM stock " +
                        "WHERE 1=1 " +
                        "<if test='keyword != null and keyword != \"\"'>" +
                        "  AND item_name LIKE CONCAT('%', #{keyword}, '%') " +
                        "</if>" +
                        "</script>")
        int countTotal(@Param("keyword") String keyword);

        @Insert("INSERT INTO stock (item_name, quantity, unit, expiration_date) " +
                        "VALUES (#{itemName}, #{quantity}, #{unit}, #{expirationDate})")
        void insertStock(StockDTO stock);

        @org.apache.ibatis.annotations.Delete("DELETE FROM stock WHERE id = #{id}")
        void deleteStock(int id);

        @org.apache.ibatis.annotations.Update("UPDATE stock SET quantity = #{quantity} WHERE id = #{id}")
        void updateQuantity(@org.apache.ibatis.annotations.Param("id") int id,
                        @org.apache.ibatis.annotations.Param("quantity") int quantity);
}
