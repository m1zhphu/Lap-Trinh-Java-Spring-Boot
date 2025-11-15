package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {
    private Long id; // ID của CartItem
    private int quantity;
    private double price;
    private double subtotal;
    
    // --- THÔNG TIN MỚI TỪ VARIANT ---
    private Integer productVariantId; // ID của biến thể (size)
    private String size; // Tên size (S, M, L...)

    // --- THÔNG TIN TỪ PRODUCT CHA ---
    private Integer productId;
    private String productName;
    private String productImage;
}