package com.example.demo.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; // <-- Đảm bảo import đúng
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter, CustomUserDetailsService userDetailsService) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }

    // Bean mã hóa mật khẩu
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean AuthenticationProvider
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Bean AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Bean Cấu hình CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Cho phép React
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Bean SecurityFilterChain (Chuỗi lọc bảo mật)
    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Áp dụng CORS
            .csrf(csrf -> csrf.disable()) // Tắt CSRF
            .authorizeHttpRequests(auth -> auth

                    // === 1. QUY TẮC CÔNG KHAI (permitAll) ===
                    // (Giữ nguyên)
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/uploads/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/banners/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/menus/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/posts/public/**").permitAll()

                    // === 2. QUY TẮC YÊU CẦU ĐĂNG NHẬP (authenticated) ===
                    // (Giữ nguyên)
                    .requestMatchers("/api/users/me").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/api/users/me/update").authenticated()
                    .requestMatchers("/api/cart/**").authenticated()

                    // Thêm các quy tắc cho phép USER truy cập Order của họ
                    // (Phải đặt TRƯỚC quy tắc /api/orders/** của ADMIN)
                    
                    .requestMatchers(HttpMethod.POST, "/api/orders/place").authenticated() // Cho phép user đặt hàng
                    .requestMatchers(HttpMethod.GET, "/api/orders/my-history").authenticated() // Cho phép user xem lịch sử của họ
                    .requestMatchers(HttpMethod.GET, "/api/orders/{orderId}").authenticated() // Cho phép user xem chi tiết đơn hàng
                    // ---------------------------------

                    // === 3. QUY TẮC ADMIN (hasRole) ===
                    // (Giữ nguyên)
                    .requestMatchers(HttpMethod.POST, "/api/products/**", "/api/categories/**", "/api/banners/**",
                            "/api/menus/**", "/api/files/**")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/products/**", "/api/categories/**", "/api/banners/**",
                            "/api/menus/**")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/products/**", "/api/categories/**", "/api/banners/**",
                            "/api/menus/**")
                    .hasRole("ADMIN")

                    .requestMatchers("/api/admin/orders/**").hasRole("ADMIN") // Cho phép Admin truy cập controller mới
                    .requestMatchers("/api/users/**").hasRole("ADMIN")
                    .requestMatchers("/api/posts/**").hasRole("ADMIN")

                    // Quy tắc của ADMIN cho Orders phải nằm SAU quy tắc của User
                    .requestMatchers("/api/orders", "/api/orders/**").hasRole("ADMIN") // Admin quản lý tất cả Orders
                    
                    // === 4. QUY TẮC CUỐI CÙNG ===
                    .anyRequest().authenticated()) // Mọi request còn lại đều cần đăng nhập
            
            // Cấu hình còn lại giữ nguyên
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
}
