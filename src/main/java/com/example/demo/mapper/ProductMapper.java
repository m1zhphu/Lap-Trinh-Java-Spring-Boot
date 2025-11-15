package com.example.demo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductVariantDto;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductVariant;

public class ProductMapper {

    // --- Chuyển đổi Variant ---
    public static ProductVariantDto toVariantDto(ProductVariant variant) {
        return ProductVariantDto.builder()
                .id(variant.getId())
                .size(variant.getSize())
                .quantity(variant.getQuantity())
                .build();
    }

    public static ProductVariant toVariantEntity(ProductVariantDto dto) {
        return ProductVariant.builder()
                // .id(dto.getId()) // ID nên được quản lý bởi JPA
                .size(dto.getSize())
                .quantity(dto.getQuantity())
                .build();
    }

    // --- Chuyển đổi Product ---
    public static ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }

        // Chuyển đổi danh sách variants
        List<ProductVariantDto> variantDtos = product.getVariants().stream()
                .map(ProductMapper::toVariantDto)
                .collect(Collectors.toList());
        
        // Tính tổng số lượng
        int totalQuantity = product.getVariants().stream()
                .mapToInt(ProductVariant::getQuantity)
                .sum();

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .salePrice(product.getSalePrice())
                .image(product.getImage())
                .status(product.isStatus())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .variants(variantDtos) // <-- Thêm list variant DTO
                .totalQuantity(totalQuantity) // <-- Thêm tổng số lượng
                .build();
    }
    
    // Hàm toEntity sẽ không map variants, service sẽ làm việc đó
    public static Product toEntity(ProductDto dto) {
         if (dto == null) {
            return null;
        }
        return Product.builder()
                .id(dto.getId()) // Cần cho việc update
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .salePrice(dto.getSalePrice())
                .image(dto.getImage())
                .status(dto.isStatus())
                .build();
        // Category và Variants sẽ được Service xử lý riêng
    }
}