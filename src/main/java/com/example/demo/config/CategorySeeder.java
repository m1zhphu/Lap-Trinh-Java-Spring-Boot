package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order; // Import Order
import org.springframework.stereotype.Component;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;

@Component
@Order(1) // Chạy file này đầu tiên
public class CategorySeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public CategorySeeder(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            Category aonu = Category.builder()
                .name("Áo Nữ")
                .description("Các loại áo thời nữ")
                .image("aonu.jpg") // <-- Thêm tên ảnh mẫu
                .build();
            Category giaynu = Category.builder()
                .name("Giày Nữ")
                .description("Các loại giày dép nữ")
                .image("giaynu.jpg") // <-- Thêm tên ảnh mẫu
                .build();
            Category tuixachnu = Category.builder()
                .name("Túi Xách Nữ")
                .description("Túi xách sang trọng")
                .image("tuixachnu.jpg") // <-- Thêm tên ảnh mẫu
                .build();
            Category tuixachnam = Category.builder()
                .name("Túi Xách Nam")
                .description("Túi xách sang trọng")
                .image("tuixachnam.jpg") // <-- Thêm tên ảnh mẫu
                .build();
            Category giaynam = Category.builder()
                .name("Giày Nam")
                .description("Giày thể thao, giày nam")
                .image("giaynam.jpg") // <-- Thêm tên ảnh mẫu
                .build();
            Category aonam = Category.builder()
                .name("Áo Nam")
                .description("Các loại thời trang nam")
                .image("aonam.jpg") // <-- Thêm tên ảnh mẫu
                .build();

            categoryRepository.save(aonu);
            categoryRepository.save(giaynu);
            categoryRepository.save(tuixachnu);
            categoryRepository.save(tuixachnam);
            categoryRepository.save(giaynam);
            categoryRepository.save(aonam);

            System.out.println(">>>> Seed dữ liệu cho Categories thành công!");
        }
    }
}