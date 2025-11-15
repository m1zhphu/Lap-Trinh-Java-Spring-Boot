package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query; // THÊM IMPORT NÀY
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Product;

@Repository
// PHẢI KẾ THỪA JpaSpecificationExecutor ĐỂ DÙNG LOGIC LỌC ĐỘNG
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    // (Giữ nguyên các phương thức cũ nếu cần, nhưng chúng ta sẽ dùng Specification để thay thế)
    
    // Thêm một phương thức mới sử dụng @Query để buộc JPA phải lấy cả Category.
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category")
    List<Product> findAllWithCategory();

    // 1. Tìm sản phẩm theo tên, không phân biệt hoa/thường (Nếu không dùng Specification)
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // 2. Tìm sản phẩm theo khoảng giá
    List<Product> findByPriceBetween(Double min, Double max);
 
    // 3. Tìm sản phẩm có số lượng lớn hơn giá trị cho trước

    List<Product> findByCategoryId(Integer categoryId);
}