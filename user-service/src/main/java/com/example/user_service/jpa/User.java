package com.example.user_service.jpa;

import com.example.user_service.dto.UserDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @CreationTimestamp
    @Column(nullable = false, updatable = false) // updatable = false 설정으로 업데이트 시 시간 변경 방지
    private LocalDateTime createAt;

    public User(UserDto dto) {
        this.userId = dto.getUserId();
        this.name = dto.getName();
        this.email = dto.getEmail();
    }
}