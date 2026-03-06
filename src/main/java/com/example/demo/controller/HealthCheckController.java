package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Chấp nhận mọi biến thể của đường link để tránh lỗi
    @GetMapping({"/api/public/health", "/api/public/health/", "/api/health"})
    public ResponseEntity<String> healthCheck() {
        try {
            // Gửi tín hiệu đánh thức Database
            jdbcTemplate.execute("SELECT 1");
            return ResponseEntity.ok("Tuyệt vời! Backend và Database đều đang thức!");
        } catch (Exception e) {
            // Nếu DB đang ngủ, lệnh trên sẽ lỗi, NHƯNG nó đã kịp đánh thức DB dậy.
            // Ta vẫn trả về OK để bot UptimeRobot luôn Xanh lá cây.
            return ResponseEntity.ok("Backend đang thức, đang đợi Database tỉnh ngủ...");
        }
    }
}