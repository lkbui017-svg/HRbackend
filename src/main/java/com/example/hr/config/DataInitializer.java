package com.example.hr.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.hr.entity.AppUser;
import com.example.hr.entity.Role;
import com.example.hr.repository.AppUserRepository;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner createDefaultAdmin(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!appUserRepository.existsByUsername("admin")) {
                AppUser admin = new AppUser(
                        "admin",
                        "admin@company.com",
                        passwordEncoder.encode("admin123"),
                        Role.ADMIN
                );
                appUserRepository.save(admin);
            }
        };
    }
}
