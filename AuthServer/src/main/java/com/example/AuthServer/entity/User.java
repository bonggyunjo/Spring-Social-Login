package com.example.AuthServer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "password", nullable = false)
    private String password; // pw 대신 password로 사용


    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name ="nickname", unique=true, length = 50)
    private String nickname;

    @Column(name = "profile_image", length = 255)
    private String profileImage;

    @Column(name = "kakao_id", nullable = true) // nullable을 true로 설정
    private String kakaoId; // 카카오 ID

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "user_type") // 구분 컬럼 추가
    private String userType; // 'normal' 또는 'social'
}
