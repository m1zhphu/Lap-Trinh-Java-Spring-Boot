package com.example.demo.config; // Hoặc com.example.demo.seeder

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Menu;
import com.example.demo.repository.MenuRepository;

import lombok.RequiredArgsConstructor;

@Component // Đánh dấu để Spring quản lý Bean này
@RequiredArgsConstructor // Lombok: Tự tạo constructor với các final fields
public class MenuSeeder implements CommandLineRunner {

    private final MenuRepository menuRepository; // Inject Repository

    @Override
    public void run(String... args) throws Exception {
        // Chỉ chạy seeder nếu bảng menus chưa có dữ liệu
        if (menuRepository.count() == 0) {
            createSampleMenus();
        }
    }

    private void createSampleMenus() {
        // === Menu Chính (ví dụ: vị trí 'main_menu') ===
        Menu home = new Menu(null, "Trang chủ", "/", 1, null, "main_menu", true);
        Menu products = new Menu(null, "Sản phẩm", "/products", 2, null, "main_menu", true);
        // Menu categories = new Menu(null, "Danh mục", "/categories", 3, null, "main_menu", true); // Hoặc link đến /products
        Menu news = new Menu(null, "Bài viết", "/posts", 4, null, "main_menu", true);
        Menu contact = new Menu(null, "Liên hệ", "/contact", 5, null, "main_menu", true);
        Menu about = new Menu(null, "Về chúng tôi", "/about", 6, null, "main_menu", true);

        // === Menu Footer (ví dụ: vị trí 'footer_menu') ===
        Menu privacy = new Menu(null, "Chính sách bảo mật", "/privacy-policy", 1, null, "footer_menu", true);
        Menu terms = new Menu(null, "Điều khoản sử dụng", "/terms-of-service", 2, null, "footer_menu", true);

        // Lưu tất cả menu vào database
        menuRepository.saveAll(List.of(
                home, products, /* categories, */ news, contact, about,
                privacy, terms
        ));

        // --- Tạo menu con cho "Sản phẩm" (Nâng cao - Tùy chọn) ---
        // Lưu ý: Cần ID của menu "Sản phẩm" đã được lưu ở trên.
        // Menu savedProductsMenu = menuRepository.findByNameAndPosition("Sản phẩm", "main_menu").orElse(null);
        // if (savedProductsMenu != null) {
        //     Menu ao = new Menu(null, "Áo", "/products/category/1", 1, savedProductsMenu.getId(), "main_menu", true); // Giả sử ID danh mục Áo là 1
        //     Menu quan = new Menu(null, "Quần", "/products/category/2", 2, savedProductsMenu.getId(), "main_menu", true); // Giả sử ID danh mục Quần là 2
        //     menuRepository.saveAll(List.of(ao, quan));
        // }

        System.out.println("--- Sample Menus seeded ---");
    }
}