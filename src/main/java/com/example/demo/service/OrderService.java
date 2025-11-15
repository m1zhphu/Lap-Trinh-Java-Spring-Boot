// src/main/java/com/example/demo/service/OrderService.java
package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.OrderDto;
import com.example.demo.dto.PlaceOrderRequestDto;
public interface OrderService {
    /**
     * Xử lý logic đặt hàng
     * @param userId Id của người dùng đặt hàng
     * @param orderRequest Thông tin người nhận
     * @return OrderDto của đơn hàng vừa tạo
     */
    OrderDto placeOrder(Integer userId, PlaceOrderRequestDto orderRequest);
    List<OrderDto> getOrderHistory(Integer userId);
    OrderDto getOrderDetail(Integer userId, Long orderId);

    // --- THÊM 2 HÀM MỚI CHO ADMIN ---
    List<OrderDto> getAllOrders();
    OrderDto getOrderDetailForAdmin(Long orderId);
    OrderDto updateOrderStatus(Long orderId, String newStatus);
}