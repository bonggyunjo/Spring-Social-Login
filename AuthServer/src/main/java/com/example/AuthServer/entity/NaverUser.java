package com.example.AuthServer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "naver_users")
public class NaverUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "naver_id", unique = true )
    private String naverId; // 네이버 ID

    @Column(length = 100)
    private String email; // 이메일 주소

    @Column(length = 50)
    private String nickname; // 별명

    @Column(name = "profile_image", length = 255)
    private String profileImage; // 프로필 사진

    @Column(length = 10)
    private String gender; // 성별

    @Column(name = "user_id")
    private Long userId; // users 테이블의 외래 키

    // User 엔티티와의 관계 설정
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    public NaverUser(String naverId, String email, String nickname, String profileImage, String gender) {
        this.naverId = naverId;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.gender = gender;
    }
}