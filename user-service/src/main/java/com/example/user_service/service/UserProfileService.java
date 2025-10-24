package com.example.user_service.service;

import com.example.user_service.exception.UserProfileException;
import com.example.user_service.jpa.UserProfile;
import com.example.user_service.jpa.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository repository;

    @Value("${user.profile.dir}")
    private String uploadDir;

    public String setImage(Long id, MultipartFile file) {
        try {
            // 디렉토리가 존재하지 않으면 생성
            Path path = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(path);

            // 파일 이름 중복 방지
            String originalFilename = file.getOriginalFilename();
            String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;
            Path filePath = path.resolve(uniqueFilename);

            // 파일 저장
            file.transferTo(filePath.toFile());

            // 디비에 저장
            repository.setImage(id, String.valueOf(filePath));

            return uniqueFilename;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new UserProfileException("프로필 파일을 저장하는데 문제가 생겼습니다.", e);
        }
    }

    public Resource getImage(Long id) {
        UserProfile userProfile = repository.getImage(id);
        String filename = userProfile.getProfileImagePath();

        // Paths.get()에 filename만 사용
        return new FileSystemResource(Paths.get(filename).toFile());
    }

    public Long deleteImage(Long id) {
        repository.deleteImage(id);
        return id;
    }
}
