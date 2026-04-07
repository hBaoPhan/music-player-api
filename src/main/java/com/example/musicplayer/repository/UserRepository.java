package com.example.musicplayer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.musicplayer.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    User findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
