package com.example.hr.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hr.entity.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
