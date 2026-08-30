package com.example.hr.dto;

public record LoginResponse(
        String token,
        String username,
        String role
) {
}
