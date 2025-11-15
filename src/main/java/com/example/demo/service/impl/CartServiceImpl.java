package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AddToCartRequestDto;
import com.example.demo.dto.CartDto;
import com.example.demo.dto.CartItemDto;
import com.example.demo.dto.UpdateCartItemRequestDto;
import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductVariant; // THÊM IMPORT
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.CartItemMapper;
import com.example.demo.mapper.CartMapper;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
// import com.example.demo.repository.ProductRepository; // Bỏ import này
import com.example.demo.repository.ProductVariantRepository; // THÊM IMPORT
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    // Bỏ repo Product, thay bằng Variant
    // private final ProductRepository productRepository; 
    private final ProductVariantRepository productVariantRepository; // THÊM REPO NÀY
    private final UserRepository userRepository;

    /**
     * Hàm tiện ích: Lấy giỏ hàng (nếu có) hoặc tạo một giỏ hàng mới (nếu chưa có).
     */
    private Cart getOrCreateCart(Integer userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
                    
                    Cart newCart = Cart.builder()
                                        .user(user)
                                        .items(new ArrayList<>()) 
                                        .totalItems(0)
                                        .totalPrice(0.0)
                                        .build();
                    return cartRepository.save(newCart); // Lưu vào DB
                });
    }
    
    /**
     * Helper: Tìm giỏ hàng. Nếu không tồn tại, trả về Cart rỗng (không lưu vào DB).
     */
    private Cart findOrCreateEmptyCart(Integer userId) {
         return cartRepository.findByUserId(userId)
                 .orElseGet(() -> {
                     // Trả về một đối tượng Cart rỗng, không lưu vào DB
                     return Cart.builder()
                            .user(userRepository.findById(userId).orElse(null))
                            .items(new ArrayList<>())
                            .totalItems(0)
                            .totalPrice(0.0)
                            .build();
                 });
    }


    @Override
    @Transactional(readOnly = true)
    public CartDto getCartByUserId(Integer userId) {
        // Luôn sử dụng findOrCreateEmptyCart để tránh lỗi 500 khi Cart bị xóa
        Cart cart = findOrCreateEmptyCart(userId);
        
        cart.recalculateTotals(); 
        
        return CartMapper.toDto(cart);
    }

    // --- HÀM ADD ITEM VIẾT LẠI HOÀN TOÀN ---
    @Override
    public CartItemDto addItemToCart(Integer userId, AddToCartRequestDto requestDto) {
        Cart cart = getOrCreateCart(userId);
        
        // 1. Tìm BIẾN THỂ (SIZE) thay vì sản phẩm
        // (Lưu ý: AddToCartRequestDto đã được sửa để nhận productVariantId)
        ProductVariant variant = productVariantRepository.findById(requestDto.getProductVariantId())
                .orElseThrow(() -> new ResourceNotFoundException("Product Variant (Size) not found with id: " + requestDto.getProductVariantId()));
        
        Product product = variant.getProduct(); // Lấy sản phẩm cha
        
        // 2. Kiểm tra tồn kho
        if (variant.getQuantity() < requestDto.getQuantity()) {
            throw new RuntimeException("Số lượng tồn kho không đủ. Chỉ còn " + variant.getQuantity() + " sản phẩm.");
        }

        // 3. Kiểm tra xem item (với size này) đã có trong giỏ hàng chưa
        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartIdAndProductVariantId(cart.getId(), variant.getId());

        CartItem cartItem;
        if (existingItemOpt.isPresent()) {
            // Đã có: Chỉ cập nhật số lượng
            cartItem = existingItemOpt.get();
            int newQuantity = cartItem.getQuantity() + requestDto.getQuantity();
            
            // Kiểm tra lại tồn kho khi cộng dồn
            if (variant.getQuantity() < newQuantity) {
                 throw new RuntimeException("Số lượng tồn kho không đủ. Bạn đã có " + cartItem.getQuantity() + " trong giỏ.");
            }
            cartItem.setQuantity(newQuantity);

        } else {
            // Chưa có: Tạo CartItem mới
            double price = (product.getSalePrice() != null && product.getSalePrice() > 0)
                            ? product.getSalePrice()
                            : product.getPrice();
            
            cartItem = CartItem.builder()
                    .cart(cart)
                    .productVariant(variant) // <-- THAY ĐỔI QUAN TRỌNG: Gán variant
                    .quantity(requestDto.getQuantity())
                    .price(price) 
                    .build();
        }
        
        cartItem.recalculateSubtotal(); 
        CartItem savedItem = cartItemRepository.save(cartItem);

        // Cập nhật tổng giỏ hàng
        cart.recalculateTotals();
        cartRepository.save(cart);

        return CartItemMapper.toDto(savedItem);
    }

    // --- HÀM UPDATE VIẾT LẠI ---
    @Override
    public CartItemDto updateCartItem(Integer userId, Long cartItemId, UpdateCartItemRequestDto requestDto) {
        Cart cart = getOrCreateCart(userId);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem not found with id: " + cartItemId));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
             throw new SecurityException("User does not have permission to update this cart item");
        }

        // Kiểm tra tồn kho của variant trước khi cập nhật
        ProductVariant variant = cartItem.getProductVariant();
        if (variant.getQuantity() < requestDto.getQuantity()) {
             throw new RuntimeException("Số lượng tồn kho không đủ. Chỉ còn " + variant.getQuantity() + " sản phẩm.");
        }

        cartItem.setQuantity(requestDto.getQuantity());
        cartItem.recalculateSubtotal();
        CartItem updatedItem = cartItemRepository.save(cartItem);

        // Cập nhật tổng giỏ hàng
        cart.recalculateTotals();
        cartRepository.save(cart);

        return CartItemMapper.toDto(updatedItem);
    }

    @Override
    public void removeCartItem(Integer userId, Long cartItemId) {
        // 1. Lấy Cart (đối tượng chính)
        Cart cart = getOrCreateCart(userId); 

        // 2. Tìm CartItem
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem not found with id: " + cartItemId));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new SecurityException("User does not have permission to remove this cart item");
        }
        
        // (Logic xóa cũ của bạn đã đúng)
        if (cart.getItems().remove(cartItem)) {
            cartItemRepository.delete(cartItem);
            cartItemRepository.flush(); 
            cart.recalculateTotals(); 
            cartRepository.save(cart);
        } else {
            throw new ResourceNotFoundException("CartItem not found in user's cart.");
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public int getCartItemCount(Integer userId) {
        // Dùng findOrCreateEmptyCart để đảm bảo không gọi DB nếu đã bị xóa, nhưng vẫn trả về 0
        Cart cart = findOrCreateEmptyCart(userId);
        cart.recalculateTotals(); 
        return cart.getTotalItems();
    }
}