package com.example.demo.controller;

import com.example.demo.exception.GlobalExceptionHandler;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 1. Chỉ test lớp ProductController
@WebMvcTest(controllers = {ProductController.class, GlobalExceptionHandler.class})
class ProductControllerTest {

    // 2. Tiêm MockMvc để thực hiện các cuộc gọi HTTP giả
    @Autowired
    private MockMvc mockMvc;

    // 3. Tạo một Mock Bean giả cho ProductService
    // Controller sẽ được tiêm service giả này, thay vì service thật
    @MockBean
    private ProductService productService;

    /**
     * Test Yêu cầu 3.4: Kiểm tra ngoại lệ khi ID không tồn tại (từ phía Controller)
     * Mục tiêu: Test xem khi Service ném lỗi, Controller có trả về
     * đúng response 404 Not Found JSON hay không.
     */
    @Test
    void testGetProductById_WhenNotFound_ShouldReturn404() throws Exception {
        // Arrange (Sắp xếp)
        Integer id = 99;
        String errorMessage = "Product not found with id: " + id;

        // Giả lập rằng khi productService.getProductById(99) được gọi,
        // nó sẽ ném ra ResourceNotFoundException
        when(productService.getProductById(id))
                .thenThrow(new ResourceNotFoundException(errorMessage));

        // Act & Assert (Hành động & Kiểm tra)
        // Thực hiện gọi GET /api/products/99
        mockMvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isNotFound()) // Mong đợi HTTP Status 404
                // Kiểm tra nội dung JSON trả về có khớp với file ErrorDetails không
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", is(errorMessage)))
                .andExpect(jsonPath("$.path", is("/api/products/" + id)));
    }

    // Bạn có thể thêm các test case khác cho Controller tại đây
    // Ví dụ: testGetProductById_Success(), testGetAllProducts_Success()...
}