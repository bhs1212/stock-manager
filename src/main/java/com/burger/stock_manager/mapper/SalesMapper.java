package com.burger.stock_manager.mapper;

import com.burger.stock_manager.model.SalesStatDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SalesMapper {

    // 일별 통계
    @Select("SELECT menu_name as menuName, SUM(sell_count) as totalCount " +
            "FROM sales_log " +
            "WHERE DATE(sale_date) = CURDATE() " +
            "GROUP BY menu_name ORDER BY totalCount DESC")
    List<SalesStatDTO> getDailyStats();

    // 주별 통계
    @Select("SELECT menu_name as menuName, SUM(sell_count) as totalCount " +
            "FROM sales_log " +
            "WHERE YEARWEEK(sale_date, 1) = YEARWEEK(CURDATE(), 1) " +
            "GROUP BY menu_name ORDER BY totalCount DESC")
    List<SalesStatDTO> getWeeklyStats();

    // 월별 통계
    @Select("SELECT menu_name as menuName, SUM(sell_count) as totalCount " +
            "FROM sales_log " +
            "WHERE YEAR(sale_date) = YEAR(CURDATE()) AND MONTH(sale_date) = MONTH(CURDATE()) " +
            "GROUP BY menu_name ORDER BY totalCount DESC")
    List<SalesStatDTO> getMonthlyStats();

    @Select("SELECT menu_name as menuName, SUM(sell_count) as totalCount " +
            "FROM sales_log " +
            "WHERE YEAR(sale_date) = #{year} AND MONTH(sale_date) = #{month} " +
            "GROUP BY menu_name ORDER BY totalCount DESC")
    List<SalesStatDTO> getStatsByMonth(@Param("year") int year, @Param("month") int month);
}