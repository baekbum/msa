package com.example.user_service.jpa;

import com.example.user_service.dto.UserDto;
import com.example.user_service.enums.UserStatus;
import com.example.user_service.vo.InsertUser;
import com.example.user_service.vo.UpdateUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false)
    private Long teamId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false) // updatable = false 설정으로 업데이트 시 시간 변경 방지
    private LocalDateTime createAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public User(InsertUser insertInfo) {
        this.userId = insertInfo.getId();
        this.name = insertInfo.getName();
        this.password = insertInfo.getPassword();
        this.email = insertInfo.getEmail();
        this.teamId = insertInfo.getTeamId();
        this.status = UserStatus.ACTIVE;
    }

    public void updateValue(UpdateUser updateInfo) {
        if (StringUtils.hasText(updateInfo.getName())) {
            this.name = updateInfo.getName();
        }

        if (StringUtils.hasText(updateInfo.getPassword())) {
            this.password = updateInfo.getPassword();
        }

        if (StringUtils.hasText(updateInfo.getEmail())) {
            this.email = updateInfo.getEmail();
        }

        if (updateInfo.getTeamId() != null) {
            this.teamId = updateInfo.getTeamId();
        }

        if (StringUtils.hasText(updateInfo.getStatus())) {
            this.status = UserStatus.valueOf(updateInfo.getStatus());
        }
    }
}