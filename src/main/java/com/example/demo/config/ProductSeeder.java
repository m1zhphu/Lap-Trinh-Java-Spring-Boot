package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductVariant; // Thêm import
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ProductVariantRepository; // Thêm import

import java.util.List; // Thêm import

@Component
@Order(2) // Chạy sau CategorySeeder
public class ProductSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository; // Thêm repo này

    /**
     * Constructor được cập nhật để inject ProductVariantRepository
     */
    public ProductSeeder(CategoryRepository categoryRepository, 
                         ProductRepository productRepository, 
                         ProductVariantRepository productVariantRepository) { // Cập nhật constructor
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.productVariantRepository = productVariantRepository; // Thêm
    }

    @Override
    public void run(String... args) throws Exception {
        // Chỉ chạy nếu có category và chưa có product
        if (categoryRepository.count() > 0 && productRepository.count() == 0) {
            System.out.println(">>>> Bắt đầu seed dữ liệu cho Products và ProductVariants...");

            Category tuixachnam = categoryRepository.findByName("Túi Xách Nam").orElseThrow();
            Category tuixachnu = categoryRepository.findByName("Túi Xách Nữ").orElseThrow();
            Category giaynu = categoryRepository.findByName("Giày Nữ").orElseThrow();
            Category giaynam = categoryRepository.findByName("Giày Nam").orElseThrow();
            Category aonam = categoryRepository.findByName("Áo Nam").orElseThrow();
            Category aonu = categoryRepository.findByName("Áo Nữ").orElseThrow();
            
            // --- TÚI XÁCH (Dùng "One Size") ---
            // Xóa .quantity() khỏi Product.builder()
            Product tuixachnu1 = Product.builder().name("OnTheGo PM").description("...").price(1250000.0).salePrice(999000.0).category(tuixachnu).image("OnTheGo PM.jpg").status(true).build();
            productRepository.save(tuixachnu1); // 1. Lưu Product trước để lấy ID
            // 2. Tạo Variant và liên kết với ID
            productVariantRepository.save(ProductVariant.builder().product(tuixachnu1).size("One Size").quantity(30).build());

            Product tuixachnu2 = Product.builder().name("Speedy Bandouliere 25").description("...").price(780000.0).category(tuixachnu).image("Speedy Bandouliere 25.jpg").status(true).build();
            productRepository.save(tuixachnu2);
            productVariantRepository.save(ProductVariant.builder().product(tuixachnu2).size("One Size").quantity(50).build());
            
            Product tuixachnu3 = Product.builder().name("Speedy Soft 30 Teddy").description("...").price(1490000.0).category(tuixachnu).image("Speedy Soft 30 Teddy.jpg").status(true).build();
            productRepository.save(tuixachnu3);
            productVariantRepository.save(ProductVariant.builder().product(tuixachnu3).size("One Size").quantity(40).build());

            Product tuixachnu4 = Product.builder().name("Túi Alma BB").description("...").price(950000.0).salePrice(899000.0).category(tuixachnu).image("Túi Alma BB.jpg").status(true).build();
            productRepository.save(tuixachnu4);
            productVariantRepository.save(ProductVariant.builder().product(tuixachnu4).size("One Size").quantity(35).build());
            
            Product tuixachnu5 = Product.builder().name("Túi Noé-Where Vivienne").description("...").price(1100000.0).salePrice(949000.0).category(tuixachnu).image("Túi Noé-Where Vivienne.jpg").status(true).build();
            productRepository.save(tuixachnu5);
            productVariantRepository.save(ProductVariant.builder().product(tuixachnu5).size("One Size").quantity(25).build());

            // --- TÚI XÁCH (Dùng "One Size") ---
            // Xóa .quantity() khỏi Product.builder()
            Product tuixachnam1 = Product.builder().name("Keepall Bandoulière 25").description("...").price(1250000.0).salePrice(999000.0).category(tuixachnam).image("Keepall Bandoulière 25.jpg").status(true).build();
            productRepository.save(tuixachnam1); // 1. Lưu Product trước để lấy ID
            // 2. Tạo Variant và liên kết với ID
            productVariantRepository.save(ProductVariant.builder().product(tuixachnam1).size("One Size").quantity(30).build());

            Product tuixachnam2 = Product.builder().name("Túi Nil").description("...").price(780000.0).category(tuixachnam).image("Túi Nil.jpg").status(true).build();
            productRepository.save(tuixachnam2);
            productVariantRepository.save(ProductVariant.builder().product(tuixachnam2).size("One Size").quantity(50).build());
            
            Product tuixachnam3 = Product.builder().name("Túi City Keepall").description("...").price(1490000.0).category(tuixachnam).image("Túi City Keepall.jpg").status(true).build();
            productRepository.save(tuixachnam3);
            productVariantRepository.save(ProductVariant.builder().product(tuixachnam3).size("One Size").quantity(40).build());

            Product tuixachnam4 = Product.builder().name("Túi Đeo Chéo Avenue PM").description("...").price(950000.0).salePrice(899000.0).category(tuixachnam).image("Túi Đeo Chéo Avenue PM.jpg").status(true).build();
            productRepository.save(tuixachnam4);
            productVariantRepository.save(ProductVariant.builder().product(tuixachnam4).size("One Size").quantity(35).build());
            
            Product tuixachnam5 = Product.builder().name("Túi Keepall Bandoulière 25").description("...").price(1100000.0).salePrice(949000.0).category(tuixachnam).image("Túi Keepall Bandoulière 25.jpg").status(true).build();
            productRepository.save(tuixachnam5);
            productVariantRepository.save(ProductVariant.builder().product(tuixachnam5).size("One Size").quantity(25).build());

            // --- GIÀY NỮ (Dùng size số) ---
            Product giaynu1 = Product.builder().name("Dép LV Mare Mule").description("...").price(680000.0).salePrice(549000.0).category(giaynu).image("Dép LV Mare Mule.jpg").status(true).build();
            productRepository.save(giaynu1);
            productVariantRepository.saveAll(List.of(
                ProductVariant.builder().product(giaynu1).size("36").quantity(10).build(),
                ProductVariant.builder().product(giaynu1).size("37").quantity(15).build(),
                ProductVariant.builder().product(giaynu1).size("38").quantity(5).build()
            ));

            Product giaynu2 = Product.builder().name("Dép Silhouette Mule").description("...").price(720000.0).category(giaynu).image("Dép Silhouette Mule.jpg").status(true).build();
            productRepository.save(giaynu2);
            productVariantRepository.saveAll(List.of(
                ProductVariant.builder().product(giaynu2).size("37").quantity(20).build(),
                ProductVariant.builder().product(giaynu2).size("38").quantity(20).build(),
                ProductVariant.builder().product(giaynu2).size("39").quantity(10).build()
            ));

            Product giaynu3 = Product.builder().name("Giày Cao Gót Gala").description("...").price(1150000.0).category(giaynu).image("Giày Cao Gót Gala.jpg").status(true).build();
            productRepository.save(giaynu3);
            productVariantRepository.saveAll(List.of(
                ProductVariant.builder().product(giaynu3).size("36").quantity(10).build(),
                ProductVariant.builder().product(giaynu3).size("37").quantity(10).build(),
                ProductVariant.builder().product(giaynu3).size("38").quantity(10).build(),
                ProductVariant.builder().product(giaynu3).size("39").quantity(10).build()
            ));

            Product giaynu4 = Product.builder().name("Giày Thể Thao LV Olympia").description("...").price(990000.0).salePrice(890000.0).category(giaynu).image("Giày Thể Thao LV Olympia.jpg").status(true).build();
            productRepository.save(giaynu4);
             productVariantRepository.saveAll(List.of(
                ProductVariant.builder().product(giaynu4).size("36").quantity(15).build(),
                ProductVariant.builder().product(giaynu4).size("37").quantity(20).build()
            ));

            Product giaynu5 = Product.builder().name("Xăng-đan LV Sunset Flat Comfort").description("...").price(850000.0).category(giaynu).image("Xăng-đan LV Sunset Flat Comfort.jpg").status(true).build();
            productRepository.save(giaynu5);
            productVariantRepository.saveAll(List.of(
                ProductVariant.builder().product(giaynu5).size("37").quantity(10).build(),
                ProductVariant.builder().product(giaynu5).size("38").quantity(15).build()
            ));
            

            // --- GIÀY NAM (Dùng size số) ---
            Product giaynam1 = Product.builder().name("LV BUTTERSOFT Sneaker").description("...").price(1350000.0).salePrice(1199000.0).category(giaynam).image("LV BUTTERSOFT Sneaker.jpg").status(true).build();
            productRepository.save(giaynam1);
            productVariantRepository.saveAll(List.of(
                ProductVariant.builder().product(giaynam1).size("40").quantity(10).build(),
                ProductVariant.builder().product(giaynam1).size("41").quantity(10).build(),
                ProductVariant.builder().product(giaynam1).size("42").quantity(10).build()
            ));

            Product giaynam2 = Product.builder().name("Giày Thể Thao LV BUTTERSOFT").description("...").price(1200000.0).category(giaynam).image("Giày Thể Thao LV BUTTERSOFT.jpg").status(true).build();
            productRepository.save(giaynam2);
            productVariantRepository.saveAll(List.of(
                ProductVariant.builder().product(giaynam2).size("40").quantity(25).build(),
                ProductVariant.builder().product(giaynam2).size("41").quantity(25).build()
            ));

            Product giaynam3 = Product.builder().name("Giày Thể Thao LV Soft").description("...").price(1800000.0).category(giaynam).image("Giày Thể Thao LV Soft.jpg").status(true).build();
            productRepository.save(giaynam3);
            productVariantRepository.saveAll(List.of(
                ProductVariant.builder().product(giaynam3).size("41").quantity(20).build(),
                ProductVariant.builder().product(giaynam3).size("42").quantity(10).build(),
                ProductVariant.builder().product(giaynam3).size("43").quantity(10).build()
            ));

            Product giaynam4 = Product.builder().name("Giày Lười LV Oxford").description("...").price(1650000.0).salePrice(1499000.0).category(giaynam).image("Giày Lười LV Oxford.jpg").status(true).build();
            productRepository.save(giaynam4);
            productVariantRepository.saveAll(List.of(
                ProductVariant.builder().product(giaynam4).size("40").quantity(10).build(),
                ProductVariant.builder().product(giaynam4).size("41").quantity(25).build()
            ));

            Product giaynam5 = Product.builder().name("Giày Thể Thao LV Runner Tatic").description("...").price(1750000.0).salePrice(1590000.0).category(giaynam).image("Giày Thể Thao LV Runner Tatic.jpg").status(true).build();
            productRepository.save(giaynam5);
            productVariantRepository.saveAll(List.of(
                ProductVariant.builder().product(giaynam5).size("41").quantity(10).build(),
                ProductVariant.builder().product(giaynam5).size("42").quantity(15).build()
            ));


            // --- ÁO NAM (Dùng size chữ) ---
            Product aonam1 = Product.builder().name("Áo Khoác Nam").description("...").price(750000.0).salePrice(649000.0).category(aonam).image("Áo Khoác Nam.jpg").status(true).build();
            productRepository.save(aonam1);
            productVariantRepository.saveAll(List.of(
                ProductVariant.builder().product(aonam1).size("S").quantity(5).build(),
                ProductVariant.builder().product(aonam1).size("M").quantity(15).build(),
                ProductVariant.builder().product(aonam1).size("L").quantity(10).build()
            ));

            Product aonam2 = Product.builder().name("Áo Dệt Kim Họa Tiết Monogram").description("...").price(480000.0).category(aonam).image("Áo Dệt Kim Họa Tiết Monogram.jpg").status(true).build();
            productRepository.save(aonam2);
            productVariantRepository.saveAll(List.of(
                ProductVariant.builder().product(aonam2).size("S").quantity(20).build(),
                ProductVariant.builder().product(aonam2).size("M").quantity(30).build()
            ));

            Product aonam3 = Product.builder().name("Áo Len").description("...").price(550000.0).category(aonam).image("Áo Len.jpg").status(true).build();
            productRepository.save(aonam3);
            productVariantRepository.saveAll(List.of(
                ProductVariant.builder().product(aonam3).size("M").quantity(20).build(),
                ProductVariant.builder().product(aonam3).size("L").quantity(20).build()
            ));

            Product aonam4 = Product.builder().name("Áo Thun").description("...").price(320000.0).salePrice(279000.0).category(aonam).image("Áo Thun.jpg").status(true).build();
            productRepository.save(aonam4);
            productVariantRepository.saveAll(List.of(
                ProductVariant.builder().product(aonam4).size("M").quantity(20).build(),
                ProductVariant.builder().product(aonam4).size("L").quantity(10).build(),
                ProductVariant.builder().product(aonam4).size("XL").quantity(5).build()
            ));

            Product aonam5 = Product.builder().name("Áo Hoodie").description("...").price(680000.0).category(aonam).image("Áo Hoodie.jpg").status(true).build();
            productRepository.save(aonam5);
            productVariantRepository.saveAll(List.of(
                ProductVariant.builder().product(aonam5).size("S").quantity(10).build(),
                ProductVariant.builder().product(aonam5).size("M").quantity(15).build()
            ));

            
            // --- ÁO NỮ (Dùng size chữ) ---
            Product aonu1 = Product.builder().name("Áo Thun LV x TM").description("...").price(380000.0).salePrice(329000.0).category(aonu).image("Áo Thun LV x TM.jpg").status(true).build();
            productRepository.save(aonu1);
            productVariantRepository.saveAll(List.of(
                ProductVariant.builder().product(aonu1).size("S").quantity(10).build(),
                ProductVariant.builder().product(aonu1).size("M").quantity(20).build()
            ));

            Product aonu2 = Product.builder().name("Áo Khoác Họa Tiết Monogram").description("...").price(850000.0).category(aonu).image("Áo Khoác Họa Tiết Monogram.jpg").status(true).build();
            productRepository.save(aonu2);
            productVariantRepository.saveAll(List.of(
                ProductVariant.builder().product(aonu2).size("S").quantity(20).build(),
                ProductVariant.builder().product(aonu2).size("M").quantity(30).build()
            ));

            Product aonu3 = Product.builder().name("Áo Khoác").description("...").price(790000.0).salePrice(699000.0).category(aonu).image("Áo Khoác.jpg").status(true).build();
            productRepository.save(aonu3);
            productVariantRepository.saveAll(List.of(
                ProductVariant.builder().product(aonu3).size("M").quantity(15).build(),
                ProductVariant.builder().product(aonu3).size("L").quantity(25).build()
            ));

            Product aonu4 = Product.builder().name("Áo Khoác Họa Tiết Kẻ Ô").description("...").price(920000.0).category(aonu).image("Áo Khoác Họa Tiết Kẻ Ô.jpg").status(true).build();
            productRepository.save(aonu4);
            productVariantRepository.saveAll(List.of(
                ProductVariant.builder().product(aonu4).size("S").quantity(10).build(),
                ProductVariant.builder().product(aonu4).size("M").quantity(25).build()
            ));

            Product aonu5 = Product.builder().name("Áo Len Nữ").description("...").price(580000.0).salePrice(499000.0).category(aonu).image("Áo Len Nữ.jpg").status(true).build();
            productRepository.save(aonu5);
            productVariantRepository.saveAll(List.of(
                ProductVariant.builder().product(aonu5).size("S").quantity(10).build(),
                ProductVariant.builder().product(aonu5).size("M").quantity(15).build()
            ));

            System.out.println(">>>> Seed dữ liệu cho Products và ProductVariants thành công!");
        }
    }
}