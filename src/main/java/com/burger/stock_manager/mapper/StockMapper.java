package com.burger.stock_manager.mapper;

import com.burger.stock_manager.model.StockDTO;
import com.burger.stock_manager.model.RecipeDTO;
import com.burger.stock_manager.model.SalesLogDTO;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface StockMapper {
        // @Select 쿼리를 <script>를 사용하는 동적 쿼리로 변경 (keyword 처리)
        @Select("<script>" +
                        "SELECT id, item_name as itemName, quantity, unit, expiration_date as expirationDate FROM stock "
                        +
                        "WHERE is_deleted = 0 " +
                        "<if test='keyword != null and keyword != \"\"'>" +
                        "  AND item_name LIKE CONCAT('%', #{keyword}, '%') " +
                        "</if>" +
                        "ORDER BY itemName ASC " +
                        "LIMIT #{offset}, #{size}" +
                        "</script>")
        List<StockDTO> findAll(@Param("keyword") String keyword,
                        @Param("offset") int offset,
                        @Param("size") int size);

        // 전체 페이지 번호를 계산하기 위해 총 개수를 가져오는 쿼리 추가
        @Select("<script>" +
                        "SELECT COUNT(*) FROM stock " +
                        "WHERE is_deleted = 0 " +
                        "<if test='keyword != null and keyword != \"\"'>" +
                        "  AND item_name LIKE CONCAT('%', #{keyword}, '%') " +
                        "</if>" +
                        "</script>")
        int countTotal(@Param("keyword") String keyword);

        @Insert("INSERT INTO stock (item_name, quantity, unit, expiration_date) " +
                        "VALUES (#{itemName}, #{quantity}, #{unit}, #{expirationDate})")
        void insertStock(StockDTO stock);

        // 삭제: DELETE 대신 UPDATE 사용 (논리 삭제)
        @Update("UPDATE stock SET is_deleted = 1 WHERE id = #{id}")
        void deleteStock(int id);

        @Update("UPDATE stock SET quantity = #{quantity} WHERE id = #{id}")
        void updateQuantity(@Param("id") int id, @Param("quantity") int quantity);

        @Select("SELECT stock_id as stockId, required_quantity as requiredQuantity " +
                        "FROM recipe WHERE menu_name = #{menuName}")
        List<RecipeDTO> getRecipeByMenu(String menuName);

        @Update("UPDATE stock SET quantity = quantity - #{usedAmount} WHERE id = #{stockId}")
        void decreaseStock(@Param("stockId") int stockId, @Param("usedAmount") int usedAmount);

        // 이름으로 기존 데이터가 있는지 확인 (is_deleted 상관없이 이름만 같으면 가져옴)
        @Select("SELECT id, item_name as itemName FROM stock WHERE item_name = #{itemName} LIMIT 1")
        StockDTO findByNameIncludeDeleted(String itemName);

        // 기존 ID의 데이터를 다시 활성화 (is_deleted를 0으로)
        @Update("UPDATE stock SET quantity = #{quantity}, unit = #{unit}, " +
                        "expiration_date = #{expirationDate}, is_deleted = 0 WHERE id = #{id}")
        void restoreStock(StockDTO stock);

        // 판매 내역 저장
        @Insert("INSERT INTO sales_log (menu_name, sell_count) VALUES (#{menuName}, #{sellCount})")
        void insertSalesLog(@Param("menuName") String menuName, @Param("sellCount") int sellCount);

        // 전체 판매 내역 조회 (최신순)
        @Select("SELECT id, menu_name as menuName, sell_count as sellCount, sale_date as saleDate " +
                        "FROM sales_log ORDER BY sale_date DESC LIMIT 50")
        List<SalesLogDTO> findAllSalesLogs();

        @Select("SELECT id, item_name as itemName, quantity FROM stock WHERE id = #{id}")
        StockDTO findStockById(int id);
}
