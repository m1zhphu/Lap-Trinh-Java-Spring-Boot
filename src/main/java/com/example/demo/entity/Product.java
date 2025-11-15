package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType; // Thêm import
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType; // Thêm import
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany; // Thêm import
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList; // Thêm import
import java.util.List; // Thêm import

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    private Double price;
    
    // private Integer quantity; // <-- XÓA CỘT NÀY

    @Column(name = "sale_price")
    private Double salePrice;

    private String image;
    
    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY) // Nên dùng LAZY
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties("products")
    private Category category;

    // --- THÊM LIÊN KẾT MỚI ---
    @OneToMany(
        mappedBy = "product", // Ánh xạ tới trường "product" trong ProductVariant
        cascade = CascadeType.ALL, // Lưu/Xóa/Cập nhật các variant CÙNG LÚC với product
        orphanRemoval = true, // Tự động xóa variant mồ côi
        fetch = FetchType.EAGER // Tải các variant ngay khi tải product
    )
    @Builder.Default // Khởi tạo list rỗng
    private List<ProductVariant> variants = new ArrayList<>();
}