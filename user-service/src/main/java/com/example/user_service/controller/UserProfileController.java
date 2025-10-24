package com.example.user_service.controller;

import com.example.user_service.exception.UserProfileException;
import com.example.user_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateImage(@PathVariable("id") Long id, MultipartFile file) {
        log.info("user profile update start");
        String path = userProfileService.setImage(id, file);
        log.info("path : {}", path);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 프로필 사진 경로를 반환하거나 이미지 자체를 반환하는 로직 추가해야함
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getImage(@PathVariable("id") Long id) {
        log.info("user profile get start");
        try {
            Resource resource = userProfileService.getImage(id);

            // 파일의 MIME 타입 결정
            String contentType = Files.probeContentType(resource.getFile().toPath());
            if (contentType == null) {
                contentType = "application/octet-stream"; // 기본 MIME 타입
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (IOException e) {
            throw new UserProfileException("사진을 가져오는데 문제가 생겼습니다.", e);
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable("id") Long id) {
        log.info("user profile delete start");
        Long deleteId = userProfileService.deleteImage(id);
        log.info("deleteId : {}", deleteId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
