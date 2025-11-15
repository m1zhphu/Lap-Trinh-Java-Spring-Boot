// src/main/java/com/example/demo/controller/PostController.java
package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PostDto;
import com.example.demo.service.PostService;

@RestController
@RequestMapping("/api/posts") // Đường dẫn gốc
@CrossOrigin(origins = "*") // Cho phép React gọi
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // === API CÔNG KHAI (Cho người dùng) ===

    @GetMapping("/public") // VD: /api/posts/public
    public ResponseEntity<List<PostDto>> getAllPublishedPosts() {
        List<PostDto> posts = postService.getAllPublishedPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/public/{slug}") // VD: /api/posts/public/bai-viet-dau-tien
    public ResponseEntity<PostDto> getPublishedPostBySlug(@PathVariable String slug) {
        PostDto post = postService.getPublishedPostBySlug(slug);
        return ResponseEntity.ok(post);
    }


    // === API ADMIN (Cần quyền Admin) ===

    @GetMapping // VD: /api/posts (Admin)
    public ResponseEntity<List<PostDto>> getAllPostsAdmin() {
        List<PostDto> posts = postService.getAllPostsAdmin();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}") // VD: /api/posts/1 (Admin)
    public ResponseEntity<PostDto> getPostById(@PathVariable Integer id) {
        PostDto post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @PostMapping // VD: /api/posts (Admin)
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        PostDto createdPost = postService.createPost(postDto);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @PutMapping("/{id}") // VD: /api/posts/1 (Admin)
    public ResponseEntity<PostDto> updatePost(@PathVariable Integer id, @RequestBody PostDto postDto) {
        PostDto updatedPost = postService.updatePost(id, postDto);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}") // VD: /api/posts/1 (Admin)
    public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}