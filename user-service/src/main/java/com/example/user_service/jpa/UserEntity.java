package com.example.user_service.jpa;

import com.example.user_service.dto.UserDto;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String email;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, unique = true)
    private String userId;
    @Column(nullable = false, unique = true)
    private String encryptedPwd;

    public UserEntity(UserDto dto) {
        this.email = dto.getEmail();
        this.name = dto.getName();
        this.userId = dto.getUserId();
    }

}