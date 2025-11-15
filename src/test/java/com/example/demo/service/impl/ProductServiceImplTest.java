package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.dto.ProductDto;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.repository.ProductRepository;

// Sử dụng Mockito
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    // 1. Tạo một đối tượng giả (mock) cho Repository
    // Service của bạn phụ thuộc vào nó
    @Mock
    private ProductRepository productRepository;

    // 2. Tiêm (inject) các mock (như productRepository) vào Service
    @InjectMocks
    private ProductServiceImpl productService; // Đây là lớp chúng ta muốn test

    private Product product;
    private ProductDto productDto;
    private Category category;

    // Hàm này chạy trước mỗi @Test
    @BeforeEach
    void setUp() {
        // Chuẩn bị dữ liệu mẫu
        category = new Category();
        category.setId(1);
        category.setName("Electronics");

        product = new Product();
        product.setId(1);
        product.setName("Laptop");
        product.setPrice(1000.0);
        product.setCategory(category);

        productDto = new ProductDto();
        productDto.setId(1);
        productDto.setName("Laptop");
        productDto.setPrice(1000.0);
        productDto.setCategoryId(1);
    }

    /**
     * Test Yêu cầu 3.4: Kiểm tra ngoại lệ khi ID không tồn tại
     */
    @Test
    void testGetProductById_WhenNotFound_ShouldThrowException() {
        // Arrange (Sắp xếp)
        Integer id = 99;
        // Giả lập rằng khi gọi findById(99) trên repository, nó sẽ trả về Optional.empty()
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert (Hành động & Kiểm tra)
        // Kiểm tra xem khi gọi productService.getProductById(99)
        // thì một exception ResourceNotFoundException CÓ được ném ra
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> {
                    productService.getProductById(id);
                }
        );

        // Kiểm tra xem message của exception có đúng không
        assertEquals("Product not found with id: " + id, exception.getMessage());
        // Đảm bảo hàm findById(99) đã được gọi đúng 1 lần
        verify(productRepository, times(1)).findById(id);
    }

    /**
     * Test Yêu cầu 3.3: Kiểm tra lấy sản phẩm theo ID (thành công)
     */
    @Test
    void testGetProductById_WhenFound_ShouldReturnProductDto() {
        // Vì ProductMapper.toDto là static, chúng ta cần "mock" nó
        try (MockedStatic<ProductMapper> mockedMapper = mockStatic(ProductMapper.class)) {
            // Arrange
            // Giả lập findById(1) trả về product mẫu
            when(productRepository.findById(1)).thenReturn(Optional.of(product));

            // SỬA LỖI Ở DÒNG NÀY:
            // Thay vì any(Product.class), chúng ta chỉ định rõ `product` từ hàm setUp
            mockedMapper.when(() -> ProductMapper.toDto(product)).thenReturn(productDto);

            // Act
            ProductDto result = productService.getProductById(1);

            // Assert
            assertNotNull(result);
            assertEquals(productDto.getId(), result.getId());
            assertEquals(productDto.getName(), result.getName());
            
            // Xác minh rằng cả hai mock đã được gọi
            verify(productRepository, times(1)).findById(1);
            mockedMapper.verify(() -> ProductMapper.toDto(product), times(1));
        }
    }

    /**
     * Test Yêu cầu 3.2: Kiểm tra thêm sản phẩm thành công
     */
    @Test
    void testAddProduct_ShouldReturnSavedProductDto() {
        try (MockedStatic<ProductMapper> mockedMapper = mockStatic(ProductMapper.class)) {
            // Arrange
            // Giả lập mapper
            mockedMapper.when(() -> ProductMapper.toEntity(any(ProductDto.class))).thenReturn(product);
            mockedMapper.when(() -> ProductMapper.toDto(any(Product.class))).thenReturn(productDto);
            
            // Giả lập repository.save()
            when(productRepository.save(any(Product.class))).thenReturn(product);

            // Act
            ProductDto result = productService.addProduct(productDto);

            // Assert
            assertNotNull(result);
            assertEquals(productDto.getName(), result.getName());
            // Đảm bảo hàm save được gọi 1 lần
            verify(productRepository, times(1)).save(any(Product.class));
        }
    }

    /**
     * Test Yêu cầu 3.5: Kiểm tra số lượng sản phẩm trả về đúng
     */
    @Test
    void testGetAllProducts_ShouldReturnCorrectCount() {
        try (MockedStatic<ProductMapper> mockedMapper = mockStatic(ProductMapper.class)) {
            // Arrange
            // Tạo một product thứ 2
            Product product2 = new Product();
            product2.setId(2);
            product2.setName("Mouse");

            // Giả lập repository trả về 2 sản phẩm
            when(productRepository.findAllWithCategory()).thenReturn(List.of(product, product2));
            
            // Giả lập mapper (chỉ cần nó chạy)
            mockedMapper.when(() -> ProductMapper.toDto(any(Product.class))).then(invocation -> new ProductDto());

            // Act
            List<ProductDto> results = productService.getAllProducts();

            // Assert
            assertNotNull(results);
            assertEquals(2, results.size()); // Kiểm tra số lượng
            verify(productRepository, times(1)).findAllWithCategory();
        }
    }
}