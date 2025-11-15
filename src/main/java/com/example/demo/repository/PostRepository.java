// src/main/java/com/example/demo/repository/PostRepository.java
package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    // Tìm các bài viết đã được 'status = true' (cho trang người dùng)
    List<Post> findByStatus(boolean status);

    // Tìm một bài viết bằng 'slug' (cho trang chi tiết người dùng)
    Optional<Post> findBySlug(String slug);
}