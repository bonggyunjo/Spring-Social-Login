package com.example.AuthServer.service;

import com.example.AuthServer.dto.UserInfo;
import com.example.AuthServer.repository.UserRepository;
import com.example.AuthServer.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // BCryptPasswordEncoder를 사용하여 비밀번호 해싱
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(UserInfo userDto) {
        User user = new User();
        user.setPassword(passwordEncoder.encode(userDto.getPassword())); // 비밀번호 해싱
        user.setEmail(userDto.getEmail());
        user.setNickname(userDto.getNickname());
        user.setProfileImage(userDto.getProfileImage());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setUserType("일반"); // 일반 사용자로 설정

        return userRepository.save(user);
    }
}
