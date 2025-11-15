// src/main/java/com/example/demo/dto/ProductFilterParamsDto.java
package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilterParamsDto {
    private String keyword;
    private String sortBy;
    private String sortDir;
    private Double minPrice;
    private Double maxPrice;
    private Boolean status = true; // Mặc định chỉ lấy sản phẩm active
}