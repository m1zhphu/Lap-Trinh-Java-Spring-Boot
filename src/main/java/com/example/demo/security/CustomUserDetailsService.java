package com.example.demo.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User; // Import User entity
import com.example.demo.repository.UserRepository;
// CustomUserDetails thường cùng package nên không cần import, nhưng thêm nếu khác

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true) // Nên thêm Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tìm User entity trong database bằng username (hoặc email)
        User user = userRepository.findByUsername(username) // Hoặc findByEmail
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với username: " + username));

        // === THAY ĐỔI QUAN TRỌNG ===
        // Trả về một đối tượng CustomUserDetails chứa User entity
        return new CustomUserDetails(user);
    }
}