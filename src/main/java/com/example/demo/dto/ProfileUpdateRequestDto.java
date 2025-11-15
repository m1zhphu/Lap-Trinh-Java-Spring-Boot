package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ProfileUpdateRequestDto {
    // Không bao gồm id, username, password, role
    
    private String name;
    
    @Email(message = "Email không hợp lệ")
    private String email;
    
    private String phoneNumber;
    private String address;
}
