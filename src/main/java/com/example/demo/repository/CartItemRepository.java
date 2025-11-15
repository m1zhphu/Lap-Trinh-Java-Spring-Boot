package com.example.demo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
    // --- THAY ĐỔI HÀM NÀY ---
    // Tìm item trong giỏ hàng dựa trên ID của GIỎ HÀNG và ID của BIẾN THỂ (SIZE)
    Optional<CartItem> findByCartIdAndProductVariantId(Long cartId, Integer productVariantId);
}