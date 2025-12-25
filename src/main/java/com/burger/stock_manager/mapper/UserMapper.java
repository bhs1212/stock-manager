package com.burger.stock_manager.mapper;

import com.burger.stock_manager.model.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    // 기존 login 메서드 (아이디, 비번 둘 다 체크하던 방식 - 나중에 지워도 됨)
    @Select("SELECT * FROM users WHERE username = #{username} AND password = #{password}")
    UserDTO login(String username, String password);

    // ★ 새로 추가할 메서드: 아이디로만 유저 정보를 가져옵니다. ★
    @Select("SELECT * FROM users WHERE username = #{username}")
    UserDTO findByUsername(String username);
}
