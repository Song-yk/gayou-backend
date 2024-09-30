package com.gayou.auth.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String name;
    private String profilePicture;
    private String token;

    public LoginResponse(String name, String profilePicture, String token) {
        this.name = name;
        this.profilePicture = profilePicture;
        this.token = token;
    }
}
