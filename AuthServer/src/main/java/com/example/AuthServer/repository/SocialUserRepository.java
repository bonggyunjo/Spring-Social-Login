package com.example.AuthServer.repository;

import com.example.AuthServer.entity.SocialUser; // 변경된 클래스
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialUserRepository extends JpaRepository<SocialUser, Long> {
    Optional<SocialUser> findByKakaoId(String kakaoId);
}