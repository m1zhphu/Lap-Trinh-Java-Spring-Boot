package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // Giữ import này
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CategoryDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;
    // Bỏ inject FileStorageService, ObjectMapper
    // private final FileStorageService fileStorageService;
    // private final ObjectMapper objectMapper;

    // GET /api/categories - Lấy tất cả (Public)
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // GET /api/categories/{id} - Lấy theo ID (Public)
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Integer id) {
        CategoryDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    // POST /api/categories - Tạo mới (Admin) - SỬA LẠI
    @PostMapping // <-- Bỏ consumes
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> createCategory(
            @RequestBody CategoryDto categoryDto // <-- Dùng @RequestBody
            // Bỏ RequestPart MultipartFile
    ) {
        try {
            // Không cần xử lý file ảnh ở đây nữa
            // Tên ảnh (nếu có) sẽ được gửi trực tiếp trong categoryDto.image
            // Ví dụ: React có thể upload ảnh lên 1 API riêng trước,
            //        rồi lấy tên file trả về để gán vào DTO trước khi gọi API này.

            // Giả sử categoryDto đã chứa tên ảnh (nếu có)
            CategoryDto createdCategory = categoryService.createCategory(categoryDto);
            return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Lỗi khi tạo category: " + e.getMessage());
            e.printStackTrace();
            // Trả về lỗi cụ thể hơn nếu cần (ví dụ: tên trùng lặp)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // PUT /api/categories/{id} - Cập nhật (Admin) - SỬA LẠI
    @PutMapping("/{id}") // <-- Bỏ consumes và value
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Integer id,
            @RequestBody CategoryDto categoryDto // <-- Dùng @RequestBody
            // Bỏ RequestPart MultipartFile
    ) {
        try {
            // Không cần xử lý file ảnh ở đây nữa
            // Nếu React muốn giữ ảnh cũ: không gửi trường image trong DTO, Service sẽ tự giữ.
            // Nếu React muốn đổi ảnh: upload ảnh mới lên API riêng, lấy tên file,
            //                        gán vào categoryDto.image rồi gửi DTO này.
            // Nếu React muốn xóa ảnh: gửi categoryDto.image = null hoặc ""

            CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto);
            return ResponseEntity.ok(updatedCategory);
        } catch (ResourceNotFoundException e) {
             return ResponseEntity.notFound().build();
        } catch (Exception e) {
             System.err.println("Lỗi khi cập nhật category: " + e.getMessage());
             e.printStackTrace();
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // DELETE /api/categories/{id} - Xóa (Admin) - SỬA LẠI (Bỏ xử lý file ảnh)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        try {
            // Tùy chọn: Lấy tên ảnh trước khi xóa để xóa file sau (nếu cần)
            // CategoryDto categoryToDelete = categoryService.getCategoryById(id);
            // String imageName = categoryToDelete.getImage();

            categoryService.deleteCategory(id); // Chỉ cần xóa entity

            // Tùy chọn: Gọi API xóa file ảnh riêng biệt nếu cần
            // if (imageName != null && !imageName.isEmpty()) {
            //     // fileStorageService.deleteFile(imageName, "categories"); // Gọi service xóa file
            // }

            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch(Exception e) {
             System.err.println("Lỗi khi xóa category: " + e.getMessage());
             e.printStackTrace();
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}