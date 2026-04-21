package com.example.musicplayer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private boolean reactivated;

    public JwtResponse(String accessToken) {
        this.token = accessToken;
        this.reactivated = false;
    }

    public JwtResponse(String accessToken, String refreshToken) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.reactivated = false;
    }

    public JwtResponse(String accessToken, String refreshToken, boolean reactivated) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.reactivated = reactivated;
    }

    public JwtResponse(String accessToken, boolean reactivated) {
        this.token = accessToken;
        this.reactivated = reactivated;
    }
}
