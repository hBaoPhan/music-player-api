package com.example.musicplayer.dto;

public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private boolean reactivated;

    public JwtResponse(String accessToken) {
        this.token = accessToken;
        this.reactivated = false;
    }

    public JwtResponse(String accessToken, boolean reactivated) {
        this.token = accessToken;
        this.reactivated = reactivated;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isReactivated() {
        return reactivated;
    }

    public void setReactivated(boolean reactivated) {
        this.reactivated = reactivated;
    }
}
