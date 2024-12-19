package com.example.AuthServer.repository;

import com.example.AuthServer.entity.KakaoUser; // 변경된 클래스
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KakaoUserRepository extends JpaRepository<KakaoUser, Long> {
    Optional<KakaoUser> findByKakaoId(String kakaoId);
}