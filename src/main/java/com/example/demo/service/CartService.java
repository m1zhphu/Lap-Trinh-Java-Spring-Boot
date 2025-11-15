package com.example.demo.service;

import com.example.demo.dto.AddToCartRequestDto;
import com.example.demo.dto.CartDto;
import com.example.demo.dto.CartItemDto; // Thêm import
import com.example.demo.dto.UpdateCartItemRequestDto; // Thêm import

public interface CartService {
    // Lấy giỏ hàng của người dùng hiện tại (dựa vào User ID từ token)
    CartDto getCartByUserId(Integer userId);

    // Thêm sản phẩm vào giỏ hàng
    CartItemDto addItemToCart(Integer userId, AddToCartRequestDto requestDto);

    // Cập nhật số lượng sản phẩm
    CartItemDto updateCartItem(Integer userId, Long cartItemId, UpdateCartItemRequestDto requestDto);

    // Xóa sản phẩm khỏi giỏ hàng
    void removeCartItem(Integer userId, Long cartItemId);

    // Lấy thông tin tóm tắt (số lượng item - dùng cho Header)
    int getCartItemCount(Integer userId);
}