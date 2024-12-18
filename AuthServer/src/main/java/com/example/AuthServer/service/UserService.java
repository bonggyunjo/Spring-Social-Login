package com.example.AuthServer.service;


import com.example.AuthServer.dto.UserInfo;
import com.example.AuthServer.repository.UserRepository;
import com.example.AuthServer.entity.User; // 올바른 User 엔티티 경로
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(UserInfo userDto) {
        User user = new User();
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setNickname(userDto.getNickname());
        user.setProfileImage(userDto.getProfileImage());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setUserType("normal"); // 일반 사용자로 설정

        return userRepository.save(user);
    }
}