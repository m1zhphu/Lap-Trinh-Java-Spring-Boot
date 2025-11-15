// src/main/java/com/example/demo/dto/PostDto.java
package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Integer id;
    private String title;
    private String slug;
    private String description;
    private String content;
    private String image; // Chỉ lưu tên file ảnh
    private boolean status;
}