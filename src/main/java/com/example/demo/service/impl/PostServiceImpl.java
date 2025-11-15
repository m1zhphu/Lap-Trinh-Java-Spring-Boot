// src/main/java/com/example/demo/service/impl/PostServiceImpl.java
package com.example.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.PostDto;
import com.example.demo.entity.Post;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.PostMapper;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.PostService;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // === ADMIN ===

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> getAllPostsAdmin() {
        // Admin xem tất cả
        return postRepository.findAll().stream()
            .map(PostMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto getPostById(Integer id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        return PostMapper.toDto(post);
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        // (Bạn có thể thêm logic tự tạo slug từ title ở đây nếu muốn)
        Post post = PostMapper.toEntity(postDto);
        Post savedPost = postRepository.save(post);
        return PostMapper.toDto(savedPost);
    }

    @Override
    public PostDto updatePost(Integer id, PostDto postDto) {
        Post existingPost = postRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        // Cập nhật
        existingPost.setTitle(postDto.getTitle());
        existingPost.setSlug(postDto.getSlug());
        existingPost.setDescription(postDto.getDescription());
        existingPost.setContent(postDto.getContent());
        existingPost.setImage(postDto.getImage()); // Cập nhật tên ảnh mới (nếu có)
        existingPost.setStatus(postDto.isStatus());

        Post updatedPost = postRepository.save(existingPost);
        return PostMapper.toDto(updatedPost);
    }

    @Override
    public void deletePost(Integer id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }

    // === USER (PUBLIC) ===

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> getAllPublishedPosts() {
        // User chỉ xem các bài viết status = true
        return postRepository.findByStatus(true).stream()
            .map(PostMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto getPublishedPostBySlug(String slug) {
        Post post = postRepository.findBySlug(slug)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found with slug: " + slug));
        
        // Kiểm tra xem bài viết có được công khai không
        if (!post.isStatus()) {
            throw new ResourceNotFoundException("Post is not published");
        }
        return PostMapper.toDto(post);
    }
}