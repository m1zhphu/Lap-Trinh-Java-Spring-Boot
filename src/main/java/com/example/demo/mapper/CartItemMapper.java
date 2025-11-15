package com.example.demo.mapper;

import com.example.demo.dto.CartItemDto;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductVariant;

public class CartItemMapper {

    public static CartItemDto toDto(CartItem item) {
        if (item == null) {
            return null;
        }

        ProductVariant variant = item.getProductVariant();
        Product product = (variant != null) ? variant.getProduct() : null;

        return CartItemDto.builder()
                .id(item.getId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .subtotal(item.getSubtotal())
                
                // Thông tin từ Variant
                .productVariantId(variant != null ? variant.getId() : null)
                .size(variant != null ? variant.getSize() : "N/A")

                // Thông tin từ Product cha
                .productId(product != null ? product.getId() : null)
                .productName(product != null ? product.getName() : "Sản phẩm không tồn tại")
                .productImage(product != null ? product.getImage() : null)
                .build();
    }
    
    // (Không cần toEntity vì logic service xử lý)
}