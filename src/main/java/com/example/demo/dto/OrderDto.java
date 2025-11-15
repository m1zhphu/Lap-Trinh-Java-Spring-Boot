// src/main/java/com/example/demo/dto/OrderDto.java
package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private Integer userId;
    private String customerName;
    private String address;
    private String phone;
    private String email;
    private Double totalPrice;
    private String status;
    private LocalDateTime orderDate;
    private List<OrderDetailDto> orderDetails;
}