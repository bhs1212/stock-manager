package com.burger.stock_manager.service;

import com.burger.stock_manager.mapper.UserMapper;
import com.burger.stock_manager.model.UserDTO;
import com.burger.stock_manager.model.UserSessionDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder encoder;

    // 로그인 검증 로직 (아이디/비밀번호 확인)
    public UserSessionDTO authenticate(String username, String password) {
        UserDTO user = userMapper.findByUsername(username);

        if (user != null && encoder.matches(password, user.getPassword())) {
            UserSessionDTO sessionUser = new UserSessionDTO();
            sessionUser.setId(user.getId());
            sessionUser.setUsername(user.getUsername());
            sessionUser.setName(user.getName());
            sessionUser.setRole(user.getRole());
            return sessionUser;
        }
        return null;
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