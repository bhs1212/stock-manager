package com.burger.stock_manager.mapper;

import com.burger.stock_manager.model.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    // 로그인 체크용 쿼리
    @Select("SELECT * FROM users WHERE username = #{username} AND password = #{password}")
    UserDTO login(@Param("username") String username, @Param("password") String password);
}
