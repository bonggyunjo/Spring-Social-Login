package com.example.AuthServer.repository;


import com.example.AuthServer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // 이메일로 사용자 찾기
    Optional<User> findByKakaoId(String kakaoId);
    Optional<User> findByNaverId(String naverId); // NaverId로 사용자 조회
}
