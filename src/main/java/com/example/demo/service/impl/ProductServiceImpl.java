package com.example.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductFilterParamsDto;
import com.example.demo.dto.ProductVariantDto;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductVariant; 
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import com.example.demo.service.ProductSpecification;

import lombok.RequiredArgsConstructor;
import java.util.ArrayList; 

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository; // <-- THÊM REPO NÀY

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        // 1. Chuyển đổi các trường cơ bản
        Product product = ProductMapper.toEntity(productDto);

        // 2. Load và gán Category (Sửa lỗi code cũ)
        if (productDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + productDto.getCategoryId()));
            product.setCategory(category);
        }

        // 3. Xử lý Variants
        if (productDto.getVariants() != null && !productDto.getVariants().isEmpty()) {
            List<ProductVariant> variants = new ArrayList<>();
            for (ProductVariantDto variantDto : productDto.getVariants()) {
                ProductVariant variant = ProductMapper.toVariantEntity(variantDto);
                variant.setProduct(product); // <-- Gán liên kết ngược
                variants.add(variant);
            }
            product.setVariants(variants);
        }

        // 4. Lưu (Cascade.ALL sẽ tự động lưu các variants)
        Product saved = productRepository.save(product);
        return ProductMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        ProductFilterParamsDto params = new ProductFilterParamsDto(null, "id", "desc", null, null, true);
        return getFilteredProducts(params);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProductById(Integer id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return ProductMapper.toDto(p);
    }

    @Override
    public ProductDto updateProduct(Integer id, ProductDto productDto) {
        // 1. Tìm sản phẩm hiện có
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // 2. Cập nhật các trường cơ bản
        existing.setName(productDto.getName());
        existing.setDescription(productDto.getDescription());
        existing.setPrice(productDto.getPrice());
        existing.setSalePrice(productDto.getSalePrice());
        existing.setImage(productDto.getImage());
        existing.setStatus(productDto.isStatus());
        
        // 3. Cập nhật category (Sửa lỗi code cũ)
        if (productDto.getCategoryId() != null) {
            if (existing.getCategory() == null || !existing.getCategory().getId().equals(productDto.getCategoryId())) {
                Category category = categoryRepository.findById(productDto.getCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + productDto.getCategoryId()));
                existing.setCategory(category);
            }
        } else {
            existing.setCategory(null);
        }

        // 4. Cập nhật Variants (Rất quan trọng)
        // Dùng `orphanRemoval = true` và `Cascade.ALL`, chúng ta chỉ cần xóa list cũ và thêm list mới.
        // JPA sẽ tự động tìm ra cái nào cần DELETE, UPDATE, INSERT.
        existing.getVariants().clear(); // Xóa list cũ (sẽ kích hoạt orphanRemoval)
        if (productDto.getVariants() != null && !productDto.getVariants().isEmpty()) {
             for (ProductVariantDto variantDto : productDto.getVariants()) {
                ProductVariant variant = ProductMapper.toVariantEntity(variantDto);
                variant.setProduct(existing); // Gán liên kết ngược
                existing.getVariants().add(variant); // Thêm vào list mới
            }
        }
        
        // 5. Lưu
        Product updatedProduct = productRepository.save(existing);
        return ProductMapper.toDto(updatedProduct);
    }

    @Override
    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    // --- CÁC HÀM LỌC (KHÔNG THAY ĐỔI NHIỀU) ---

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getFilteredProducts(ProductFilterParamsDto params) {
        Specification<Product> spec = ProductSpecification.getProducts(params);
        String sortBy = params.getSortBy() != null ? params.getSortBy() : "id";
        String sortDir = params.getSortDir() != null ? params.getSortDir() : "desc";
        Sort sort = Sort.by(
            sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
            sortBy
        );
        List<Product> products = productRepository.findAll(spec, sort); 
        return products.stream().map(ProductMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByCategoryId(Integer categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }
}