// src/main/java/com/example/demo/service/PostService.java
package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.PostDto;

public interface PostService {
    
    // === CHO ADMIN (Cần quyền) ===
    List<PostDto> getAllPostsAdmin(); // Lấy tất cả (cả ẩn và hiện)
    PostDto getPostById(Integer id);
    PostDto createPost(PostDto postDto);
    PostDto updatePost(Integer id, PostDto postDto);
    void deletePost(Integer id);

    // === CHO USER (Công khai) ===
    List<PostDto> getAllPublishedPosts(); // Chỉ lấy các bài viết status = true
    PostDto getPublishedPostBySlug(String slug); // Lấy chi tiết bài viết
}