// src/main/java/com/example/demo/entity/Order.java
package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.List;       // THÊM

import jakarta.persistence.CascadeType;       // THÊM
import jakarta.persistence.Column;      // THÊM
import jakarta.persistence.Entity; // THÊM
import jakarta.persistence.FetchType; // THÊM
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
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
@Table(name = "orders") // Tên bảng là 'orders'
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String customerName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double totalPrice;

    @Column(nullable = false)
    private String status; // Ví dụ: PENDING, PROCESSING, COMPLETED, CANCELED

    private LocalDateTime orderDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;
    
    @PrePersist
    protected void onCreate() {
        if (this.orderDate == null) {
            this.orderDate = LocalDateTime.now(); // Tự gán ngày giờ hiện tại
        }
        if (this.status == null) {
            this.status = "PENDING"; // Tự gán trạng thái "Chờ xác nhận"
        }
    }
}