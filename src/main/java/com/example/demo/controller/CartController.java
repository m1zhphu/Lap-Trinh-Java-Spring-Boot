package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin; // Import Principal của bạn
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping; // Thêm import
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; // Thêm import
import org.springframework.web.bind.annotation.RequestBody; // Thêm import
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; // Thêm import

import com.example.demo.dto.AddToCartRequestDto;
import com.example.demo.dto.CartDto;
import com.example.demo.dto.CartItemDto;
import com.example.demo.dto.UpdateCartItemRequestDto;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;

    // Hàm tiện ích lấy User ID từ Principal (thông tin user đang đăng nhập)
    private Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new SecurityException("User not authenticated");
        }
        // Giả sử Principal của bạn là CustomUserDetails và có getId()
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal(); 
        return userDetails.getId();
    }

    // GET /api/cart - Lấy giỏ hàng chi tiết của người dùng hiện tại
    @GetMapping
    public ResponseEntity<CartDto> getMyCart() {
        Integer userId = getCurrentUserId();
        CartDto cartDto = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cartDto);
    }

    // POST /api/cart/add - Thêm sản phẩm vào giỏ hàng
    @PostMapping("/add")
    public ResponseEntity<CartItemDto> addItemToMyCart(@Valid @RequestBody AddToCartRequestDto requestDto) {
        Integer userId = getCurrentUserId();
        CartItemDto addedItem = cartService.addItemToCart(userId, requestDto);
        return ResponseEntity.ok(addedItem);
    }

    // PUT /api/cart/update/{cartItemId} - Cập nhật số lượng item trong giỏ
    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<CartItemDto> updateMyCartItem(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequestDto requestDto) {
        Integer userId = getCurrentUserId();
        CartItemDto updatedItem = cartService.updateCartItem(userId, cartItemId, requestDto);
        return ResponseEntity.ok(updatedItem);
    }

    // DELETE /api/cart/remove/{cartItemId} - Xóa item khỏi giỏ
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<Void> removeMyCartItem(@PathVariable Long cartItemId) {
        Integer userId = getCurrentUserId();
        cartService.removeCartItem(userId, cartItemId);
        return ResponseEntity.noContent().build();
    }

    // GET /api/cart/info - Lấy số lượng item (dùng cho Header)
    @GetMapping("/info")
    public ResponseEntity<?> getMyCartInfo() {
        try {
            Integer userId = getCurrentUserId();
            int itemCount = cartService.getCartItemCount(userId);
            return ResponseEntity.ok(Map.of("itemCount", itemCount));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Map.of("itemCount", 0));
        }
    }


}