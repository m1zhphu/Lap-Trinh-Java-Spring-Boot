package com.example.demo.service; // Đảm bảo package này khớp với dòng import bạn chọn ở trên

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.example.demo.dto.ProductFilterParamsDto;
import com.example.demo.entity.Product;

import jakarta.persistence.criteria.Predicate;

public class ProductSpecification {

    public static Specification<Product> getProducts(ProductFilterParamsDto params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), params.getStatus()));
            }

            if (StringUtils.hasText(params.getKeyword())) {
                String likeKeyword = "%" + params.getKeyword().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likeKeyword));
            }

            if (params.getMinPrice() != null && params.getMinPrice() >= 0) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), params.getMinPrice()));
            }
            if (params.getMaxPrice() != null && params.getMaxPrice() > 0) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), params.getMaxPrice()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}