package com.example.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Long id; // ID của Cart
    private Integer userId;
    private List<CartItemDto> items;
    private int totalItems; // Tổng số lượng sản phẩm
    private double totalPrice; // Tổng tiền
}