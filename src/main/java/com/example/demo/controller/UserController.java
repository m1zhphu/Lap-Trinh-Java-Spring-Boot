package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; 
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ProfileUpdateRequestDto;
import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService; 

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // API lấy tất cả người dùng (ADMIN)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers(); 
        return ResponseEntity.ok(users);
    }

    // API TẠO NGƯỜI DÙNG MỚI (ADMIN)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        try {
            UserDto createdUser = userService.createUserByAdmin(userDto); 
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new UserDto(null, userDto.getUsername(), userDto.getName(), userDto.getEmail(), null, null, null, e.getMessage()));
        }
    }

    // API Lấy chi tiết User theo ID (Dùng cho Admin Edit)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) {
        UserDto userDto = userService.getUserById(id); 
        return ResponseEntity.ok(userDto);
    }
    
    // API Cập nhật thông tin User khác theo ID (Dùng cho ADMIN)
    @PutMapping("/{id}") 
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateUser(@PathVariable Integer id, @Valid @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * API LẤY THÔNG TIN USER ĐANG ĐĂNG NHẬP (CHÍNH XÁC)
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        try {
            // GỌI SERVICE THẬT ĐỂ LẤY DỮ LIỆU TỪ DB
            UserDto userDto = userService.getUserDtoByUsername(username); 
            return ResponseEntity.ok(userDto);
        } catch (RuntimeException e) { 
             System.err.println("Error fetching user /me: " + e.getMessage()); // Log lỗi
             return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
        }
    }
    
    // API cho người dùng tự cập nhật thông tin cá nhân
    @PutMapping("/me/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> updateCurrentUser(@Valid @RequestBody ProfileUpdateRequestDto updateRequest) {
        // Lấy username từ Authentication context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        
        // Gọi Service để cập nhật
        UserDto updatedUser = userService.updateSelfProfile(currentUsername, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }
    
    // API Xóa User theo ID (Dành cho ADMIN)
     @DeleteMapping("/{id}") 
     @PreAuthorize("hasRole('ADMIN')")
     public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
         userService.deleteUser(id); 
         return ResponseEntity.noContent().build();
     }
}