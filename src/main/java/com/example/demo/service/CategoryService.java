package com.example.demo.service;

import java.util.List; // <-- THÊM IMPORT DTO

import com.example.demo.dto.CategoryDto;
// import com.example.demo.entity.Category; // <-- KHÔNG CẦN IMPORT ENTITY NỮA (cho public methods)

public interface CategoryService {

    /**
     * Lấy tất cả danh mục.
     * @return Danh sách CategoryDto.
     */
    List<CategoryDto> getAllCategories(); // <-- Giữ nguyên (đã đúng)

    /**
     * Lấy chi tiết một danh mục theo ID.
     * @param id ID của danh mục.
     * @return CategoryDto.
     */
    CategoryDto getCategoryById(Integer id); // <-- SỬA KIỂU TRẢ VỀ THÀNH CategoryDto

    /**
     * Tạo một danh mục mới.
     * @param categoryDto Dữ liệu danh mục mới.
     * @return CategoryDto của danh mục đã được tạo.
     */
    CategoryDto createCategory(CategoryDto categoryDto); // <-- SỬA THAM SỐ VÀ KIỂU TRẢ VỀ THÀNH CategoryDto

    /**
     * Cập nhật thông tin một danh mục.
     * @param id ID của danh mục cần cập nhật.
     * @param categoryDto Dữ liệu cập nhật.
     * @return CategoryDto của danh mục đã được cập nhật.
     */
    CategoryDto updateCategory(Integer id, CategoryDto categoryDto); // <-- SỬA THAM SỐ VÀ KIỂU TRẢ VỀ THÀNH CategoryDto

    /**
     * Xóa một danh mục theo ID.
     * @param id ID của danh mục cần xóa.
     */
    void deleteCategory(Integer id); // <-- Giữ nguyên (đã đúng)
}