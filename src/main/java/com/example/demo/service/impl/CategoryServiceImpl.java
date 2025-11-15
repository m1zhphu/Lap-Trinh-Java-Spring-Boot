package com.example.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service; // Import Exception
import org.springframework.transaction.annotation.Transactional; // Import Mapper

import com.example.demo.dto.CategoryDto;
import com.example.demo.entity.Category;
import com.example.demo.exception.ResourceNotFoundException; // Sử dụng Lombok
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.repository.CategoryRepository; // Import Transactional
import com.example.demo.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Tự động tạo constructor
@Transactional // Áp dụng transaction cho tất cả các phương thức public
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    // Inject FileUploadService nếu cần xử lý file ảnh
    // private final FileUploadService fileUploadService;

    /**
     * Lấy tất cả danh mục.
     * @return Danh sách CategoryDto.
     */
    @Override
    @Transactional(readOnly = true) // Tối ưu cho thao tác chỉ đọc
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::toDto) // Sử dụng Mapper để chuyển đổi
                .collect(Collectors.toList());
    }

    /**
     * Lấy chi tiết một danh mục theo ID.
     * @param id ID của danh mục.
     * @return CategoryDto.
     * @throws ResourceNotFoundException nếu không tìm thấy ID.
     */
    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + id));
        return CategoryMapper.toDto(category); // Sử dụng Mapper
    }

    /**
     * Tạo một danh mục mới.
     * @param categoryDto Dữ liệu danh mục mới (bao gồm cả 'image').
     * @return CategoryDto của danh mục đã được tạo.
     */
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        // TODO: Xử lý upload file ảnh và gán tên file vào categoryDto.setImage() ở đây (nếu có file)
        // Ví dụ:
        // if (imageFile != null && !imageFile.isEmpty()) {
        //     String savedImageName = fileUploadService.storeFile(imageFile, "categories");
        //     categoryDto.setImage(savedImageName);
        // }

        Category category = CategoryMapper.toEntity(categoryDto); // Chuyển DTO thành Entity
        category.setId(null); // Đảm bảo ID là null khi tạo mới
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.toDto(savedCategory); // Chuyển Entity đã lưu thành DTO trả về
    }

    /**
     * Cập nhật thông tin một danh mục.
     * @param id ID của danh mục cần cập nhật.
     * @param categoryDto Dữ liệu cập nhật (bao gồm cả 'image' nếu có thay đổi).
     * @return CategoryDto của danh mục đã được cập nhật.
     * @throws ResourceNotFoundException nếu không tìm thấy ID.
     */
    @Override
    public CategoryDto updateCategory(Integer id, CategoryDto categoryDto) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + id));

        // TODO: Xử lý upload file ảnh mới và xóa file ảnh cũ (nếu có file mới)
        // Ví dụ:
        // String newImageName = existingCategory.getImage(); // Giữ ảnh cũ mặc định
        // if (imageFile != null && !imageFile.isEmpty()) {
        //     // Xóa ảnh cũ (nếu có)
        //     if (existingCategory.getImage() != null) {
        //         fileUploadService.deleteFile(existingCategory.getImage(), "categories");
        //     }
        //     // Lưu ảnh mới
        //     newImageName = fileUploadService.storeFile(imageFile, "categories");
        // }
        // categoryDto.setImage(newImageName); // Cập nhật tên ảnh (mới hoặc cũ) vào DTO

        // Cập nhật các trường từ DTO vào Entity đã tồn tại
        existingCategory.setName(categoryDto.getName());
        existingCategory.setDescription(categoryDto.getDescription());
        existingCategory.setImage(categoryDto.getImage()); // Cập nhật tên ảnh
        // existingCategory.setStatus(categoryDto.getStatus()); // Nếu có trường status

        Category updatedCategory = categoryRepository.save(existingCategory); // Lưu thay đổi
        return CategoryMapper.toDto(updatedCategory); // Trả về DTO đã cập nhật
    }

    /**
     * Xóa một danh mục theo ID.
     * @param id ID của danh mục cần xóa.
     * @throws ResourceNotFoundException nếu không tìm thấy ID.
     */
    @Override
    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + id));

        // TODO: Xóa file ảnh liên quan trước khi xóa entity (nếu có)
        // if (category.getImage() != null) {
        //     fileUploadService.deleteFile(category.getImage(), "categories");
        // }

        categoryRepository.delete(category);
    }
}