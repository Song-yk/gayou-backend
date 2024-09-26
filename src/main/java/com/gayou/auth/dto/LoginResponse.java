package com.gayou.auth.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String name;
    private String token;

    public LoginResponse(String name, String token) {
        this.name = name;
        this.token = token;
    }
}
