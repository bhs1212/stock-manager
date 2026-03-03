package com.burger.stock_manager.model;

import lombok.Data;

@Data
public class UserSessionDTO {
    private int id;
    private String username;
    private String name;
    private String role;
}
