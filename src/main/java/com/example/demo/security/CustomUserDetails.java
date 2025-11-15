package com.example.demo.security;

import java.util.Collection; // Import entity User của bạn
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.entity.User;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = user.getRole() != null ? user.getRole() : "USER"; // Lấy role, mặc định là USER
        // Đảm bảo role có tiền tố ROLE_
        if (!role.startsWith("ROLE_")) {
             role = "ROLE_" + role.toUpperCase();
        } else {
             role = role.toUpperCase();
        }
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        // Trả về trường dùng để đăng nhập (username hoặc email)
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        // Liên kết với trạng thái của User entity
        return true; // Giả sử User có isStatus()
    }

    // --- Phương thức quan trọng để lấy ID ---
    /**
     * Lấy ID của người dùng.
     * KIỂU DỮ LIỆU (Integer/Long) PHẢI KHỚP VỚI User entity.
     */
    public Integer getId() { // <-- HOẶC Long getId()
        return user.getId();
    }

    // --- Các phương thức tiện ích khác ---
    public User getUserEntity() { // Lấy toàn bộ User entity nếu cần
        return user;
    }

    public String getEmail() {
        return user.getEmail();
    }
}