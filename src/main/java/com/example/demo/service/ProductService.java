package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductFilterParamsDto; // THÊM IMPORT

public interface ProductService {
    ProductDto addProduct(ProductDto productDto);
    List<ProductDto> getAllProducts();
    ProductDto getProductById(Integer id);
    ProductDto updateProduct(Integer id, ProductDto productDto);
    void deleteProduct(Integer id);

    // THÊM HÀM LỌC/SẮP XẾP TỔNG HỢP NÀY
    List<ProductDto> getFilteredProducts(ProductFilterParamsDto params); 

    // BỎ CÁC HÀM TÌM KIẾM CŨ (searchByName, searchByPriceRange, searchByQuantityGreaterThan)
    // List<ProductDto> searchByName(String keyword);
    // List<ProductDto> searchByPriceRange(Double min, Double max);
    // List<ProductDto> searchByQuantityGreaterThan(Integer quantity);

    List<ProductDto> getProductsByCategoryId(Integer categoryId);
}