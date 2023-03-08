package com.example.filehostingservice.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
public class UploadController {
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String uploadDir = "/home/ling/Documents/20221/Lap trinh mang/Network_programing/storage";
        try {
            // Lưu ảnh vào thư mục bên ngoài project
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes());

            // Trả về đường dẫn để truy cập ảnh
            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/image/")
                    .path(fileName)
                    .toUriString();
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/image/{imageName:.+}")
    public ResponseEntity<FileSystemResource> getImage(@PathVariable String imageName) throws IOException {
        Path imagePath = Paths.get("/home/ling/Documents/20221/Lap trinh mang/Network_programing/storage/" + imageName);
        FileSystemResource resource = new FileSystemResource(imagePath);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .contentLength(resource.contentLength())
                .body(resource);
    }
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
