package com.flipfit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;

public class LoginRequest {
    
    @NotBlank
    @JsonProperty("identifier")
    private String identifier;
    
    @NotBlank
    @JsonProperty("password")
    private String password;
    
    @JsonProperty("role")
    private String role;

    public LoginRequest() {
    }

    public LoginRequest(String identifier, String password, String role) {
        this.identifier = identifier;
        this.password = password;
        this.role = role;
    }

    @JsonProperty
    public String getIdentifier() {
        return identifier;
    }

    @JsonProperty
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @JsonProperty
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty
    public String getRole() {
        return role;
    }

    @JsonProperty
    public void setRole(String role) {
        this.role = role;
    }
}
