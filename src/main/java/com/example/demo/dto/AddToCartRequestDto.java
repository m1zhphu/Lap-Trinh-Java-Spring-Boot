package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddToCartRequestDto {
    
    // THAY ĐỔI: Không dùng productId nữa
    // @NotNull
    // private Integer productId; 

    // DÙNG CÁI NÀY:
    @NotNull
    private Integer productVariantId; // ID của size (ví dụ: ID của "Áo Thun, Size M")

    @NotNull
    @Min(1)
    private int quantity;
}