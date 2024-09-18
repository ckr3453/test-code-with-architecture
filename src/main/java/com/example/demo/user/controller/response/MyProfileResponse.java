package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MyProfileResponse {

    private final Long id;
    private final String email;
    private final String nickname;
    private final String address;
    private final UserStatus status;
    private final Long lastLoginAt;

    @Builder
    public MyProfileResponse(Long id, String email, String nickname, String address, UserStatus status, Long lastLoginAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.address = address;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
    }

    public static MyProfileResponse from (User user) {
        return MyProfileResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .nickname(user.getNickname())
            .address(user.getAddress())
            .status(user.getStatus())
            .lastLoginAt(user.getLastLoginAt())
            .build();
    }
}
