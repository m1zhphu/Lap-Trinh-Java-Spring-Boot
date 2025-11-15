package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter; 
import lombok.NoArgsConstructor; 
import lombok.Setter;

@Getter 
@Setter 
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    // --- THAY ĐỔI LỚN ---
    // Bỏ liên kết với Product
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "product_id", nullable = false)
    // private Product product;

    // Thêm liên kết với ProductVariant
    @ManyToOne(fetch = FetchType.LAZY) // EAGER có thể tốt hơn nếu bạn luôn cần thông tin
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;
    // --- KẾT THÚC THAY ĐỔI ---

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price; // Giá của sản phẩm tại thời điểm thêm

    @Column(nullable = false)
    private double subtotal; // Thành tiền (quantity * price)

    // Hàm helper để tính lại thành tiền
    public void recalculateSubtotal() {
        // Lấy giá từ product cha của variant
        if (this.price == 0 && this.productVariant != null && this.productVariant.getProduct() != null) {
            Product product = this.productVariant.getProduct();
            // Ưu tiên giá sale nếu có
            this.price = (product.getSalePrice() != null && product.getSalePrice() > 0)
                            ? product.getSalePrice()
                            : product.getPrice();
        }
        this.subtotal = this.price * this.quantity;
    }
}