package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor
public class UserDto {
    private Integer id; 
    private String username; 
    private String name; 
    private String email; 
    private String role; 
    private String phoneNumber; 
    private String address;
    private String password; // <-- THÊM TRƯỜNG NÀY CHO MỤC ĐÍCH POST/TẠO MỚI
}