package com.example.AuthServer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NaverUserInfo {
    private String naverId;
    private String email;
    private String nickname;
    private String profileImage;
    private String gender;
}
