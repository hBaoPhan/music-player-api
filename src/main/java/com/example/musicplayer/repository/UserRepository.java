package com.example.musicplayer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.musicplayer.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    User findByEmail(String email);

    // Auth: tìm user không phân biệt trạng thái
    Optional<User> findByUsernameOrEmail(String username, String email);

    // Auth: chỉ tìm user đang active
    Optional<User> findByUsernameOrEmailAndIsActiveTrue(String username, String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findAllByIsActiveTrue();

    Optional<User> findByIdAndIsActiveTrue(Long id);

    boolean existsByIdAndIsActiveTrue(Long id);
}
