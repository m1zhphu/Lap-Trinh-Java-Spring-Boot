package com.example.demo.mapper;

import com.example.demo.dto.CategoryDto;
import com.example.demo.entity.Category;

public class CategoryMapper {

    public static CategoryDto toDto(Category category) {
        if (category == null) return null;
        return CategoryDto.builder() // Dùng builder nếu có
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .image(category.getImage()) // <-- THÊM DÒNG NÀY
                // .status(category.getStatus()) // Nếu có status
                .build();
        /* Hoặc dùng constructor nếu không có builder:
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getImage() // <-- THÊM DÒNG NÀY
                // category.getStatus() // Nếu có status
        );
        */
    }

    public static Category toEntity(CategoryDto dto) {
        if (dto == null) return null;
        return Category.builder() // Dùng builder nếu có
                .id(dto.getId()) // ID cần khi cập nhật
                .name(dto.getName())
                .description(dto.getDescription())
                .image(dto.getImage()) // <-- THÊM DÒNG NÀY
                // .status(dto.getStatus()) // Nếu có status
                .build();
        /* Hoặc dùng constructor/setter nếu không có builder:
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setImage(dto.getImage()); // <-- THÊM DÒNG NÀY
        // category.setStatus(dto.getStatus()); // Nếu có status
        return category;
         */
    }
}