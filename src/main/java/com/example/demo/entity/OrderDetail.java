// src/main/java/com/example/demo/entity/OrderDetail.java
package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;       // THÊM
import jakarta.persistence.GeneratedValue;       // THÊM
import jakarta.persistence.GenerationType;      // THÊM
import jakarta.persistence.Id; // THÊM
import jakarta.persistence.JoinColumn; // THÊM
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter // Dùng Getter
@Setter // Dùng Setter
@Builder // <-- THÊM ANNOTATION NÀY
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product; // Liên kết đến sản phẩm

    private int quantity;
    private double price; // Giá tại thời điểm mua
    private String productName; // Lưu lại tên SP phòng khi SP bị xóa/đổi tên

    @Column(nullable = false, length = 50)
    private String size; // Ví dụ: "S", "M", "One Size"
}