package com.example.user_service.jpa;

import com.example.user_service.exception.UserProfileException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserProfileRepositoryImpl implements UserProfileRepository {

    private final UserProfileJpaRepository repository;

    @Override
    public void setImage(Long id, String imagePath) {
        Optional<UserProfile> profile = repository.findById(id);

        if (profile.isPresent()) {
            log.info("user profile image update");
            UserProfile findProfile = profile.get();
            findProfile.updateImage(imagePath);
        } else {
            log.info("user profile image insert");
            UserProfile userProfile = new UserProfile(id, imagePath);
            repository.save(userProfile);
        }
    }

    @Override
    public UserProfile getImage(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserProfileException("해당 유저의 프로필 사진이 존재하지 않습니다."));
    }

    @Override
    public void deleteImage(Long id) {
        UserProfile findUserProfile = getImage(id);
        repository.delete(findUserProfile);
    }
}
