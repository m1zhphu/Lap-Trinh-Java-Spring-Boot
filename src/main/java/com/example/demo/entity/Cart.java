// src/main/java/com/example/demo/entity/Cart.java
package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List; // THÊM IMPORT NÀY

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter; // THÊM
import lombok.NoArgsConstructor; // THÊM
import lombok.Setter;

@Getter // THÊM
@Setter // THÊM
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết 1-1 với User
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // Liên kết 1-Nhiều với CartItem
    // Eager loading để khi lấy Cart sẽ lấy luôn items
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default // Khởi tạo mặc định khi dùng @Builder
    private List<CartItem> items = new ArrayList<>(); // Khởi tạo danh sách

    // Thêm các trường tổng hợp
    private double totalPrice;
    private int totalItems;

    // Hàm helper để tính toán lại tổng giỏ hàng
    public void recalculateTotals() {
        double total = 0;
        int count = 0;
        if (items != null) {
            for (CartItem item : items) {
                item.recalculateSubtotal(); // Tính lại subtotal của item
                total += item.getSubtotal(); // Cộng dồn
                count += item.getQuantity(); // Cộng dồn số lượng
            }
        }
        this.totalPrice = total;
        this.totalItems = count;
    }
}