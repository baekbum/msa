package com.example.user_service.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    private Long id;

    // 프로필 사진 파일의 경로를 저장할 필드
    private String profileImagePath;

    public void updateImage(String imagePath) {
        this.profileImagePath = imagePath;
    }

}
