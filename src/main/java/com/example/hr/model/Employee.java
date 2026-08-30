package com.example.hr.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record Employee(
        int id,
        @NotBlank String name,
        @NotBlank String department,
        @NotBlank String role,
        @Email String email,
        String status
) {
}
