package com.example.demo.controller;

import com.example.demo.dto.OrderDto; // Bạn cần tạo DTO này
import com.example.demo.dto.PlaceOrderRequestDto;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    // Hàm lấy User ID (giống CartController)
    private Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new SecurityException("User not authenticated");
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal(); 
        return userDetails.getId();
    }

    @PostMapping("/place")
    @PreAuthorize("hasRole('USER')") // Chỉ user mới được đặt hàng
    public ResponseEntity<?> placeOrder(@Valid @RequestBody PlaceOrderRequestDto orderRequest) {
        try {
            Integer userId = getCurrentUserId();
            // Hàm này cần được tạo trong OrderService
            OrderDto createdOrder = orderService.placeOrder(userId, orderRequest); 
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (RuntimeException e) {
            // Bắt các lỗi (ví dụ: giỏ hàng trống, lỗi lưu CSDL)
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    // API lấy lịch sử đơn hàng của người dùng hiện tại
    @GetMapping("/my-history")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderDto>> getMyOrderHistory() {
        Integer userId = getCurrentUserId(); // Hàm getCurrentUserId() lấy ID từ token
        List<OrderDto> orders = orderService.getOrderHistory(userId);
        return ResponseEntity.ok(orders);
    }

    // GET /api/orders/{orderId} - Lấy chi tiết một đơn hàng theo ID
    @GetMapping("/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDto> getOrderDetail(@PathVariable Long orderId) {
        Integer userId = getCurrentUserId();
        // Hàm này sẽ kiểm tra xem đơn hàng có thuộc về userId không trước khi trả về
        OrderDto orderDto = orderService.getOrderDetail(userId, orderId); 
        return ResponseEntity.ok(orderDto);
    }

    // ========================================================
    // ===           API CHO ADMIN (QUẢN TRỊ VIÊN)         ===
    // ========================================================

    /**
     * [ADMIN] Lấy TẤT CẢ đơn hàng.
     * Tương ứng với trang "Admin Order List"
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // CHỈ ADMIN
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        // Gọi hàm mới 'getAllOrders' trong OrderService
        List<OrderDto> orders = orderService.getAllOrders(); 
        return ResponseEntity.ok(orders);
    }

    /**
     * [ADMIN] Lấy CHI TIẾT 1 đơn hàng bất kỳ (không kiểm tra sở hữu).
     * Tương ứng với trang "Admin Order Detail"
     * * Chúng ta dùng đường dẫn "/admin/{orderId}" để tránh xung đột
     * với đường dẫn "/{orderId}" của User ở trên.
     */
    @GetMapping("/admin/{orderId}")
    @PreAuthorize("hasRole('ADMIN')") // CHỈ ADMIN
    public ResponseEntity<OrderDto> getOrderDetailForAdmin(@PathVariable Long orderId) {
        // Gọi hàm mới 'getOrderDetailForAdmin' trong OrderService
        OrderDto order = orderService.getOrderDetailForAdmin(orderId);
        return ResponseEntity.ok(order);
    }

    /**
     * [ADMIN] Cập nhật trạng thái của 1 đơn hàng.
     * API: PUT /api/orders/admin/{orderId}/status
     */
    @PutMapping("/admin/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDto> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody Map<String, String> statusUpdate) { // Nhận JSON dạng { "status": "SHIPPING" }

        String newStatus = statusUpdate.get("status");
        if (newStatus == null || newStatus.isEmpty()) {
            // Trả về lỗi nếu request body không có "status"
            return ResponseEntity.badRequest().build();
        }

        // Gọi service để cập nhật
        OrderDto updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.ok(updatedOrder);
    }
}