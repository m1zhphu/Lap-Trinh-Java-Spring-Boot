// src/main/java/com/example/demo/repository/OrderRepository.java
package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Order; // <<< THÊM DÒNG NÀY ĐỂ KHẮC PHỤC LỖI

public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // Thêm hàm query này để lấy lịch sử đơn hàng theo ID người dùng, sắp xếp theo ngày giảm dần
    List<Order> findByUserIdOrderByOrderDateDesc(Integer userId); 
}