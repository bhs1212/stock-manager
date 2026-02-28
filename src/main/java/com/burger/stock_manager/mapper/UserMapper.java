package com.burger.stock_manager.mapper;

import com.burger.stock_manager.model.UserDTO;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    // 아이디로만 유저 정보를 가져옵니다.
    @Select("SELECT * FROM users WHERE username = #{username}")
    UserDTO findByUsername(String username);

    // 회원가입: 사용자 정보를 DB에 저장
    @Insert("INSERT INTO users (username, password, name, role) VALUES (#{username}, #{password}, #{name}, #{role})")
    int insertUser(UserDTO user);

    // 아이디 중복 체크: 해당 아이디를 가진 사용자가 몇 명인지 반환 (0이면 중복 아님)
    @Select("SELECT COUNT(*) FROM users WHERE username = #{username}")
    int existsByUsername(String username);
}
