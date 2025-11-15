package com.example.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.ProfileUpdateRequestDto;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UserDto; 
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional 
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    // Helper: Chuyển Entity sang DTO (Trả về 8 tham số, password là null)
    private UserDto convertToUserDto(User user) {
        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getName(),
            user.getEmail(),
            user.getRole(),
            user.getPhoneNumber(),
            user.getAddress(),
            null // Tham số thứ 8: Password (luôn là null)
        );
    }
    
    // --- AUTHENTICATION & REGISTER ---

    @Override
    public String register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
             throw new RuntimeException("Email đã tồn tại!");
         }
        
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .email(request.getEmail())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .role("USER")
                .build();
        userRepository.save(user);
        return jwtService.generateToken(user.getUsername(), user.getRole());
    }

    @Override
    public String login(LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found after login"));

        return jwtService.generateToken(user.getUsername(), user.getRole());
    }

    // --- ADMIN & SELF USER MANAGEMENT IMPLEMENTATION ---
    
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return convertToUserDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserDtoByUsername(String username) {
        // THÊM LOG ĐỂ XEM USERNAME ĐƯỢC GỬI LÊN
        System.out.println("--- JWT USERNAME RECEIVED: " + username + " ---"); 

        User user = userRepository.findByUsername(username)
                 .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        
        System.out.println("--- USER FOUND IN DB: " + user.getId() + " ---");
        return convertToUserDto(user);
    }

    @Override
    public UserDto createUserByAdmin(UserDto userDto) {
         if (userRepository.existsByUsername(userDto.getUsername())) {
             throw new RuntimeException("Tên đăng nhập đã tồn tại!");
         }
         if (userRepository.existsByEmail(userDto.getEmail())) {
             throw new RuntimeException("Email đã tồn tại!");
         }
         
         if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
             throw new RuntimeException("Mật khẩu không được để trống!");
         }

         User newUser = User.builder()
                             .username(userDto.getUsername())
                             .password(passwordEncoder.encode(userDto.getPassword())) 
                             .name(userDto.getName())
                             .email(userDto.getEmail())
                             .phoneNumber(userDto.getPhoneNumber())
                             .address(userDto.getAddress())
                             .role(userDto.getRole()) 
                             .build();

         User savedUser = userRepository.save(newUser);
         return convertToUserDto(savedUser);
    }
    
    @Override
    public UserDto updateUser(Integer id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPhoneNumber(userDto.getPhoneNumber());
        existingUser.setAddress(userDto.getAddress());
        existingUser.setRole(userDto.getRole()); 
        
        User updatedUser = userRepository.save(existingUser);
        return convertToUserDto(updatedUser);
    }
    
    @Override
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
             throw new ResourceNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
        // TODO: CẦN THÊM LOGIC XÓA GIỎ HÀNG VÀ ĐƠN HÀNG CỦA USER NÀY
    }
    
    /**
     * THÊM TRIỂN KHAI CHO UPDATE SELF PROFILE
     */
    @Override
    public UserDto updateSelfProfile(String username, ProfileUpdateRequestDto updateRequest) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        existingUser.setName(updateRequest.getName());
        existingUser.setEmail(updateRequest.getEmail());
        existingUser.setPhoneNumber(updateRequest.getPhoneNumber());
        existingUser.setAddress(updateRequest.getAddress());

        User updatedUser = userRepository.save(existingUser);
        return convertToUserDto(updatedUser);
    }
}