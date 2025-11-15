package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.ProfileUpdateRequestDto;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UserDto;

public interface UserService {
    
    // --- AUTHENTICATION & SELF-SERVICE ---
    String register(RegisterRequest request);
    String login(LoginRequest request);
    UserDto getUserDtoByUsername(String username);
    UserDto updateSelfProfile(String username, ProfileUpdateRequestDto updateRequest);
    // --- ADMIN USER MANAGEMENT ---
    List<UserDto> getAllUsers();
    UserDto getUserById(Integer id);
    UserDto updateUser(Integer id, UserDto userDto); 
    void deleteUser(Integer id);
    UserDto createUserByAdmin(UserDto userDto); // API tạo mới (sử dụng UserDto có password)
}