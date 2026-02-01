package com.flipfit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {
    
    @JsonProperty("success")
    private boolean success;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("userId")
    private String userId;
    
    @JsonProperty("role")
    private String role;
    
    @JsonProperty("token")
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public LoginResponse(boolean success, String message, String userId, String role) {
        this.success = success;
        this.message = message;
        this.userId = userId;
        this.role = role;
        this.token = "Bearer " + generateToken(userId, role);
    }

    @JsonProperty
    public boolean isSuccess() {
        return success;
    }

    @JsonProperty
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    @JsonProperty
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty
    public String getUserId() {
        return userId;
    }

    @JsonProperty
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty
    public String getRole() {
        return role;
    }

    @JsonProperty
    public void setRole(String role) {
        this.role = role;
    }

    @JsonProperty
    public String getToken() {
        return token;
    }

    @JsonProperty
    public void setToken(String token) {
        this.token = token;
    }

    private String generateToken(String userId, String role) {
        return userId + ":" + role + ":" + System.currentTimeMillis();
    }
}
