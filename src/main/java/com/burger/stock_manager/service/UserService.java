package com.burger.stock_manager.service;

import com.burger.stock_manager.mapper.UserMapper;
import com.burger.stock_manager.model.UserDTO;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserMapper userMapper, BCryptPasswordEncoder encoder) {
        this.userMapper = userMapper;
        this.encoder = encoder;
    }

    // 아이디 중복 체크 로직
    public boolean isUsernameTaken(String username) {
        return userMapper.existsByUsername(username) > 0;
    }

    // 회원가입 로직 (비밀번호 암호화 포함)
    @Transactional
    public void registerUser(UserDTO user) {
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userMapper.insertUser(user);
    }
}