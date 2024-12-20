package com.example.AuthServer.service;

import com.example.AuthServer.dto.GoogleUserInfo;
import com.example.AuthServer.entity.GoogleUser;
import com.example.AuthServer.entity.User;
import com.example.AuthServer.repository.GoogleUserRepository;
import com.example.AuthServer.repository.UserRepository;
import lombok.Getter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class GoogleAuthService {

    @Getter
    @Value("${google.client.id}")
    private String clientId;

    @Getter
    @Value("${google.client.secret}")
    private String clientSecret;

    @Getter
    @Value("${google.redirect.uri}")
    private String redirectUri;


    private final GoogleUserRepository googleUserRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public GoogleAuthService(GoogleUserRepository googleUserRepository, UserRepository userRepository, RestTemplate restTemplate) {
        this.googleUserRepository = googleUserRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    public String getAccessToken(String code) throws IOException {
        String url = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = "client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&code=" + code +
                "&redirect_uri=" + redirectUri +
                "&grant_type=authorization_code";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            JSONObject jsonObject = new JSONObject(response.getBody());
            return jsonObject.getString("access_token");
        } else {
            throw new IOException("Failed to get access token: " + response.getStatusCode());
        }
    }

    public GoogleUserInfo getUserInfo(String accessToken) throws IOException {
        String userInfoUrl = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken;

        ResponseEntity<String> response = restTemplate.getForEntity(userInfoUrl, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            JSONObject jsonObject = new JSONObject(response.getBody());
            GoogleUserInfo userInfo = new GoogleUserInfo();
            userInfo.setGoogleId(jsonObject.getString("id"));
            userInfo.setNickname(jsonObject.getString("name"));
            userInfo.setEmail(jsonObject.getString("email"));
            userInfo.setProfileImage(jsonObject.getString("picture"));
            return userInfo;
        } else {
            throw new IOException("Failed to get user info: " + response.getStatusCode());
        }
    }

    public void registerUser(GoogleUserInfo userInfo) {
        GoogleUser googleUser = googleUserRepository.findByGoogleId(userInfo.getGoogleId())
                .orElse(new GoogleUser(userInfo.getGoogleId(), userInfo.getNickname(), userInfo.getProfileImage(), userInfo.getEmail()));

        googleUserRepository.save(googleUser);

        User newUser = userRepository.findByGoogleId(userInfo.getGoogleId()).orElse(null);
        if (newUser == null) {
            newUser = new User();
            newUser.setEmail(userInfo.getEmail());
            newUser.setNickname(userInfo.getNickname());
            newUser.setProfileImage(userInfo.getProfileImage());
            newUser.setGoogleId(userInfo.getGoogleId());
            newUser.setPassword("null");
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setUpdatedAt(LocalDateTime.now());
            newUser.setUserType("구글");
            userRepository.save(newUser);
        }
    }
}
