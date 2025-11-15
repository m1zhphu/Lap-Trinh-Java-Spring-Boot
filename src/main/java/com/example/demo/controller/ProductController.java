package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductFilterParamsDto;
import com.example.demo.service.ProductService; // <<-- THÊM IMPORT NÀY

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    // === API GET TẤT CẢ SẢN PHẨM (ĐÃ THÊM LỌC) ===
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(
        // @RequestParam nhận tham số từ URL
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String sortBy,
        @RequestParam(required = false) String sortDir,
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice
    ) {
        // Tạo DTO tham số (status=true để chỉ lấy sản phẩm active)
        ProductFilterParamsDto params = new ProductFilterParamsDto(keyword, sortBy, sortDir, minPrice, maxPrice, true);
        
        // Gọi Service để thực hiện lọc và sắp xếp
        List<ProductDto> products = productService.getFilteredProducts(params); 

        return ResponseEntity.ok(products);
    }
    
    // === API GET SẢN PHẨM THEO ID (Giữ nguyên) ===
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Integer id) {
        ProductDto productDto = productService.getProductById(id);
        // Lưu ý: Ở đây không dùng HATEOAS như code cũ của bạn
        return ResponseEntity.ok(productDto);
    }

    // === API GET SẢN PHẨM THEO DANH MỤC (Giữ nguyên) ===
    @GetMapping("/category/{id}")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@PathVariable Integer id) {
        List<ProductDto> products = productService.getProductsByCategoryId(id);
        return ResponseEntity.ok(products);
    }

    // === API THÊM SẢN PHẨM (ADMIN) ===
    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')") // Cần thêm PreAuthorize nếu chưa có
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto) {
        ProductDto saved = productService.addProduct(productDto);
        return ResponseEntity.ok(saved);
    }

    // === API CẬP NHẬT SẢN PHẨM (ADMIN) ===
    @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Integer id, @RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.updateProduct(id, productDto));
    }

    // === API XÓA SẢN PHẨM (ADMIN) ===
    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}