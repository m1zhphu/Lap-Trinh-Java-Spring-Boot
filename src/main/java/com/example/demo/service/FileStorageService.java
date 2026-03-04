package com.example.demo.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileStorageService {

    private final Cloudinary cloudinary;

    // Inject bean Cloudinary đã tạo ở bước 3 vào đây
    public FileStorageService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String store(MultipartFile file, String subDirectory) {
        if (file.isEmpty()) {
            throw new RuntimeException("File rỗng, không thể lưu trữ.");
        }

        try {
            // Upload file lên Cloudinary, chỉ định thư mục lưu trữ (folder) là subDirectory
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), 
                    ObjectUtils.asMap("folder", subDirectory));

            // Trả về đường dẫn URL an toàn (https) của tấm ảnh từ Cloudinary
            return uploadResult.get("secure_url").toString();
            
        } catch (IOException e) {
            throw new RuntimeException("Upload file lên Cloudinary thất bại!", e);
        }
    }
}