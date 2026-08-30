package com.example.hr.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.hr.dto.LoginRequest;
import com.example.hr.dto.LoginResponse;
import com.example.hr.entity.AppUser;
import com.example.hr.entity.Employee;
import com.example.hr.entity.Role;
import com.example.hr.repository.AppUserRepository;
import com.example.hr.repository.EmployeeRepository;
import com.example.hr.security.JwtProvider;

@Service
public class AuthenticationService {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        AppUser user = (AppUser) authentication.getPrincipal();
        String token = jwtProvider.generateToken(user);

        return new LoginResponse(
                token,
                user.getUsername(),
                user.getRole().toString()
        );
    }

    public LoginResponse register(LoginRequest request) {
        if (appUserRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already in use: " + request.username());
        }

        String email = request.username() + "@company.com";
        if (appUserRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use: " + email);
        }

        AppUser newUser = new AppUser(
                request.username(),
                email,
                passwordEncoder.encode(request.password()),
                Role.EMPLOYEE
        );

        AppUser savedUser = appUserRepository.save(newUser);

        // Create associated employee record
        try {
            Employee employee = new Employee(
                    request.username(),
                    "Unassigned",
                    "Employee",
                    email,
                    "N/A",
                    com.example.hr.entity.EmployeeStatus.ACTIVE,
                    LocalDate.now(),
                    "Permanent",
                    java.math.BigDecimal.ZERO
            );
            employee.setUser(savedUser);
            employeeRepository.save(employee);
        } catch (Exception e) {
            // If employee creation fails, delete the user
            appUserRepository.delete(savedUser);
            throw new IllegalArgumentException("Failed to create employee record: " + e.getMessage());
        }

        String token = jwtProvider.generateToken(savedUser);

        return new LoginResponse(
                token,
                savedUser.getUsername(),
                savedUser.getRole().toString()
        );
    }

    public void changePassword(String username, String oldPassword, String newPassword) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        appUserRepository.save(user);
    }
}
