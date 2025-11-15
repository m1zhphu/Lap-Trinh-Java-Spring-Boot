// src/main/java/com/example/demo/dto/OrderDetailDto.java
package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDto {
    private Long id;
    private Integer productId;
    private String productName;
    private String productImage;
    private int quantity;
    private double price;
    private double subtotal;
    private String size;
}