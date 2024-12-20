package com.example.AuthServer.repository;

import com.example.AuthServer.entity.GoogleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoogleUserRepository extends JpaRepository<GoogleUser,Long> {
    Optional<GoogleUser> findByGoogleId(String googleId);
}
