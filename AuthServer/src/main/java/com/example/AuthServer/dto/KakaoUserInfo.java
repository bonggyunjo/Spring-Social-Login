package com.example.AuthServer.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserInfo {
    private String id;
    private String KakaoId;
    private String nickname;
    private String profileImage;

}