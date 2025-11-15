package com.example.demo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.demo.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    
    Optional<Cart> findByUserId(Integer userId);

    // --- SỬA CÂU QUERY NÀY ---
    // Thay "ci.product" thành "ci.productVariant pv LEFT JOIN FETCH pv.product"
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items ci LEFT JOIN FETCH ci.productVariant pv LEFT JOIN FETCH pv.product WHERE c.user.id = :userId")
    Optional<Cart> findCartWithItemsByUserId(Integer userId); 
}