package com.example.musicplayer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.musicplayer.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
}
