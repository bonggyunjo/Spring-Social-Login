package com.example.AuthServer.repository;

import com.example.AuthServer.entity.User; // 올바른 User 엔티티 경로
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // 이메일로 사용자 찾기
    Optional<User> findByKakaoId(String kakaoId); // kakaoId로 사용자 찾기
}
