package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.OrderDetailDto;
import com.example.demo.dto.OrderDto;
import com.example.demo.dto.PlaceOrderRequestDto;
import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.ProductVariant; // THÊM IMPORT
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductVariantRepository; // THÊM IMPORT
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService { 

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    // Thêm repo của Variant để trừ kho
    private final ProductVariantRepository productVariantRepository; 

    @Override 
    @Transactional // Đây là transaction quan trọng, nếu lỗi sẽ rollback
    public OrderDto placeOrder(Integer userId, PlaceOrderRequestDto orderRequest) {
        
        // 1. Lấy user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // 2. Lấy giỏ hàng
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng"));
        
        if (cart.getItems() == null || cart.getItems().isEmpty()) { 
            throw new RuntimeException("Giỏ hàng của bạn đang trống!");
        }

        // 3. Tính toán lại tổng tiền lần cuối
        cart.recalculateTotals(); 

        // 4. Tạo Order mới
        Order order = Order.builder()
                .user(user)
                .customerName(orderRequest.getName())
                .email(orderRequest.getEmail())
                .phone(orderRequest.getPhone())
                .address(orderRequest.getAddress())
                .totalPrice(cart.getTotalPrice())
                // Status và OrderDate sẽ tự set (ví dụ: dùng @PrePersist trong Entity Order)
                .build();

        // 5. Tạo List<OrderDetail> từ CartItems VÀ TRỪ KHO
        List<OrderDetail> orderDetails = new ArrayList<>();
        
        for (CartItem item : cart.getItems()) { 
            
            // --- LOGIC TRỪ KHO (NÂNG CẤP) ---
            ProductVariant variant = item.getProductVariant(); // Lấy đúng variant (size)
            if (variant == null) {
                throw new RuntimeException("Lỗi: Không tìm thấy biến thể sản phẩm trong giỏ hàng.");
            }
            
            // 5a. Kiểm tra tồn kho lần cuối
            if (variant.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Sản phẩm " + variant.getProduct().getName() + 
                                           " (Size " + variant.getSize() + ") không đủ hàng. Chỉ còn " + variant.getQuantity());
            }
            
            // 5b. Trừ kho
            variant.setQuantity(variant.getQuantity() - item.getQuantity());
            productVariantRepository.save(variant); // Lưu lại số lượng mới của size đó
            
            // --- TẠO ORDER DETAIL (NÂNG CẤP) ---
            OrderDetail detail = OrderDetail.builder()
                    .order(order) // Liên kết lại với Order cha
                    .product(variant.getProduct()) // Liên kết với Product cha
                    .productName(variant.getProduct().getName()) // Lưu tên sản phẩm
                    .size(variant.getSize()) // <-- LƯU SIZE ĐÃ MUA
                    .price(item.getPrice()) // Lấy giá từ cart item
                    .quantity(item.getQuantity())
                    .build();
            orderDetails.add(detail);
        }
        order.setOrderDetails(orderDetails); // Set danh sách vào Order

        // 6. Lưu Order (và OrderDetails nhờ CascadeType.ALL)
        Order savedOrder = orderRepository.save(order);

        // 7. Xóa giỏ hàng
        cartRepository.delete(cart); 
        
        // 8. Map sang OrderDto để trả về
        return convertToOrderDto(savedOrder); 
    }

    // --- HÀM HELPER CONVERT SANG DTO (NÂNG CẤP) ---

    private OrderDto convertToOrderDto(Order order) {
        if (order == null) return null;
        
        List<OrderDetailDto> detailDtos = new ArrayList<>();
        if (order.getOrderDetails() != null) {
            detailDtos = order.getOrderDetails().stream()
                    .map(this::convertToOrderDetailDto)
                    .collect(Collectors.toList());
        }

        return new OrderDto(
                order.getId(),
                order.getUser() != null ? order.getUser().getId() : null,
                order.getCustomerName(),
                order.getAddress(),
                order.getPhone(),
                order.getEmail(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getOrderDate(),
                detailDtos
        );
    }

    private OrderDetailDto convertToOrderDetailDto(OrderDetail detail) {
        if (detail == null) return null;
        String productImage = null;
        if (detail.getProduct() != null) {
            productImage = detail.getProduct().getImage();
        }
        return new OrderDetailDto(
                detail.getId(),
                detail.getProduct() != null ? detail.getProduct().getId() : null,
                detail.getProductName(),
                productImage,
                detail.getQuantity(),
                detail.getPrice(),
                detail.getPrice() * detail.getQuantity(), // Tính subtotal
                detail.getSize() // <-- THÊM SIZE VÀO DTO
        );
    }

    // --- CÁC HÀM CŨ (KHÔNG ĐỔI) ---

    @Override 
    @Transactional(readOnly = true)
    public OrderDto getOrderDetail(Integer userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng #" + orderId));

        if (!order.getUser().getId().equals(userId)) {
             throw new SecurityException("Bạn không có quyền truy cập đơn hàng này.");
        }
        return convertToOrderDto(order);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrderHistory(Integer userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByOrderDateDesc(userId);
        return orders.stream()
                .map(this::convertToOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll(
            org.springframework.data.domain.Sort.by(
                org.springframework.data.domain.Sort.Direction.DESC, "orderDate"
            )
        );
        return orders.stream()
               .map(this::convertToOrderDto)
               .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderDetailForAdmin(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng #" + orderId));
        return convertToOrderDto(order);
    }

    @Override
    @Transactional
    public OrderDto updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng #" + orderId));
        order.setStatus(newStatus.toUpperCase()); 
        Order savedOrder = orderRepository.save(order);
        return convertToOrderDto(savedOrder);
    }
}