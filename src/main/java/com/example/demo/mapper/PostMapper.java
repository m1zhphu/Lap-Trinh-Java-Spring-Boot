// src/main/java/com/example/demo/mapper/PostMapper.java
package com.example.demo.mapper;

import com.example.demo.dto.PostDto;
import com.example.demo.entity.Post;

public class PostMapper {

    // Chuyển Entity sang DTO
    public static PostDto toDto(Post post) {
        if (post == null) return null;
        return new PostDto(
            post.getId(),
            post.getTitle(),
            post.getSlug(),
            post.getDescription(),
            post.getContent(),
            post.getImage(),
            post.isStatus()
        );
    }

    // Chuyển DTO sang Entity
    public static Post toEntity(PostDto dto) {
        if (dto == null) return null;
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setSlug(dto.getSlug());
        post.setDescription(dto.getDescription());
        post.setContent(dto.getContent());
        post.setImage(dto.getImage());
        post.setStatus(dto.isStatus());
        return post;
    }
}