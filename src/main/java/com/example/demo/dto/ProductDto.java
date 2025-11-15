package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List; // Thêm import

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private Double salePrice;
    
    // private Integer quantity; // <-- XÓA DÒNG NÀY
    
    private Integer categoryId;
    private String categoryName;
    private String image;
    private boolean status;

    // --- THÊM 2 TRƯỜNG MỚI ---
    // Gửi danh sách các size và số lượng của chúng
    private List<ProductVariantDto> variants; 
    
    // Gửi tổng số lượng (tính toán) để hiển thị ở trang list
    private Integer totalQuantity; 
}