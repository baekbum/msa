package com.example.user_service.jpa;

public interface UserProfileRepository {
    void setImage(Long id, String imagePath);
    UserProfile getImage(Long id);
    void deleteImage(Long id);
}
