package com.example.AuthServer.controller;

import com.example.AuthServer.dto.KakaoUserInfo;
import com.example.AuthServer.entity.SocialUser;
import com.example.AuthServer.entity.User;
import com.example.AuthServer.repository.SocialUserRepository;
import com.example.AuthServer.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.redirect.uri}")
    private String redirectUri;

    @Value("${kakao.logout.redirect.uri}")
    private String logoutRedirectUri;

    @Autowired
    SocialUserRepository userRepository;

    @Autowired
    private UserRepository user1Repository;

    @GetMapping("/kakao")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        String url = "https://kauth.kakao.com/oauth/authorize?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&response_type=code";
        System.out.println("Redirecting to: " + url); // 로그 출력
        response.sendRedirect(url);
    }

    @GetMapping("/kakao/callback")
    public RedirectView kakaoCallback(@RequestParam String code, HttpServletRequest request) {
        try {
            // Access Token 요청
            String accessToken = getAccessToken(code);
            // 사용자 정보 요청
            KakaoUserInfo userInfo = getUserInfo(accessToken);

            if (userInfo.getKakaoId() == null) {
                throw new IllegalArgumentException("Kakao ID cannot be null");
            }

            // SocialUser 저장
            SocialUser socialUser = userRepository.findByKakaoId(userInfo.getKakaoId())
                    .orElse(new SocialUser(userInfo.getKakaoId(), userInfo.getNickname(), userInfo.getProfileImage()));

            userRepository.save(socialUser); // kakao_users 테이블에 사용자 정보를 저장

            // users 테이블에 사용자 정보 저장
            User newUser = user1Repository.findByKakaoId(userInfo.getKakaoId()).orElse(null);
            if (newUser == null) {
                newUser = new User();
                newUser.setEmail("null"); // 유저 테이블에선 null값으로 설정 (소셜 로그인으로 인한)
                newUser.setNickname(userInfo.getNickname());
                newUser.setProfileImage(userInfo.getProfileImage());
                newUser.setPassword("null"); // 유저 테이블에선 null값으로 설정 (소셜 로그인으로 인한)
                newUser.setKakaoId(userInfo.getKakaoId()); // kakaoId 설정
                newUser.setCreatedAt(LocalDateTime.now());
                newUser.setUpdatedAt(LocalDateTime.now());
                newUser.setUserType("social"); // 카카오 로그인 시 'social'로 설정
                user1Repository.save(newUser); // users 테이블에 사용자 정보를 저장
            }


            // 세션에 사용자 정보 저장
            HttpSession session = request.getSession();
            session.setAttribute("user", userInfo); // 사용자 정보를 세션에 저장

            // 성공적으로 로그인 후 /success로 리다이렉트
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("http://localhost:8080/success");
            return redirectView;
        } catch (Exception e) {
            e.printStackTrace(); // 예외를 로그에 출력
            throw new RuntimeException("Error during Kakao login process", e);
        }
    }





    @GetMapping("/success")
    public ResponseEntity<?> successPage(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 현재 세션 가져오기
        if (session != null) {
            KakaoUserInfo userInfo = (KakaoUserInfo) session.getAttribute("user"); // 세션에서 사용자 정보 가져오기
            if (userInfo != null) {
                Map<String, String> response = new HashMap<>();
                response.put("nickname", userInfo.getNickname());
                response.put("profileImage", userInfo.getProfileImage());
                return ResponseEntity.ok(response); // JSON 형식으로 반환
            }
        }
        return ResponseEntity.status(401).body("Unauthorized"); // 세션 정보가 없으면 401 응답
    }
    private String getAccessToken(String     code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        OkHttpClient client = new OkHttpClient();

        FormBody formBody = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("client_id", clientId)
                .add("redirect_uri", redirectUri)
                .add("code", code)
                .build();

        Request request = new Request.Builder()
                .url(tokenUrl)
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody); // JSON 파싱
                return jsonObject.getString("access_token"); // access_token 가져오기
            } else {
                throw new RuntimeException("Failed to get access token: " + response.message());
            }
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred while getting access token", e);
        }
    }
    @PostMapping("/session")
    public ResponseEntity<?> createSession(@RequestBody KakaoUserInfo userInfo, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("user", userInfo); // 사용자 정보를 세션에 저장
        return ResponseEntity.ok("Session created successfully");
    }

    private KakaoUserInfo getUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(userInfoUrl)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody); // JSON 파싱

                KakaoUserInfo userInfo = new KakaoUserInfo();
                userInfo.setId(String.valueOf(jsonObject.getLong("id"))); // id를 Long으로 가져온 후 String으로 변환
                userInfo.setKakaoId(userInfo.getId()); // kakaoId 설정
                JSONObject properties = jsonObject.getJSONObject("properties"); // properties 객체 가져오기
                userInfo.setNickname(properties.getString("nickname")); // nickname 가져오기
                userInfo.setProfileImage(properties.getString("profile_image")); // profile_image 가져오기
                return userInfo;
            } else {
                throw new RuntimeException("Failed to get user info: " + response.message());
            }
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred while getting user info", e);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // 카카오 로그아웃 URL 생성
        String logoutUrl = "https://kauth.kakao.com/oauth/logout?client_id=" + clientId + "&logout_redirect_uri=" + logoutRedirectUri;

        return ResponseEntity.ok(logoutUrl); // 로그아웃 URL 반환
    }

    @GetMapping("/logout")
    public ResponseEntity<?> handleLogoutRedirect(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 현재 세션 가져오기
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }
        return ResponseEntity.ok("로그아웃이 완료되었습니다."); // 클라이언트에 응답
    }

}
