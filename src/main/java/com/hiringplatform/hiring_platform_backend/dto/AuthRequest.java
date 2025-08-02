package com.hiringplatform.hiring_platform_backend.dto;

/**
 * A Data Transfer Object (DTO) that represents the request body for a login attempt.
 * This class encapsulates the credentials (username and password) sent by the client
 * to the /login endpoint, providing a clean and structured way to handle authentication data.
 */
public class AuthRequest {

    private String username;
    private String password;

    // --- Getters and Setters ---

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
