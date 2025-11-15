// src/main/java/com/example/demo/entity/Post.java
package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table; // Cho nội dung dài
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String title; // Tiêu đề bài viết

    @Column(nullable = false, unique = true, length = 255)
    private String slug; // Đường dẫn (vd: /bai-viet-moi)

    @Column(length = 500)
    private String description; // Mô tả ngắn

    @Lob // Đánh dấu đây là một Large Object
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // Nội dung bài viết (dạng text dài)

    @Column(length = 255)
    private String image; // Tên file ảnh (giống như Product)

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean status = true; // Trạng thái (hiển thị/ẩn)
    
    // (Bạn có thể thêm @CreationTimestamp và @UpdateTimestamp nếu muốn)
}