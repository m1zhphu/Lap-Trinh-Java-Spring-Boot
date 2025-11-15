// src/main/java/com/example/demo/mapper/CartMapper.java
package com.example.demo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.dto.CartDto;
import com.example.demo.dto.CartItemDto;
import com.example.demo.entity.Cart;

public class CartMapper {

    public static CartDto toDto(Cart cart) {
        if (cart == null) {
            return null;
        }

        // Chuyển đổi danh sách CartItem sang CartItemDto
        List<CartItemDto> itemDtos = List.of(); // Mặc định là rỗng
        if (cart.getItems() != null) {
            itemDtos = cart.getItems().stream()
                            .map(CartItemMapper::toDto) // Gọi mapper item
                            .collect(Collectors.toList());
        }
        
        // Tính toán lại tổng (để chắc chắn)
        cart.recalculateTotals();

        return new CartDto(
            cart.getId(),
            cart.getUser() != null ? cart.getUser().getId() : null,
            itemDtos,
            cart.getTotalItems(),
            cart.getTotalPrice()
        );
    }
}