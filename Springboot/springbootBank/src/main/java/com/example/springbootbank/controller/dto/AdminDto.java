package com.example.springbootbank.controller.dto;

import com.example.springbootbank.entity.Menu;
import lombok.Data;

import java.util.List;

@Data
public class AdminDto {
    private String id;
    private String username;
    private String password;
    private String role;
    private String token;
    private List<Menu> menus;
}
