package com.example.musicplayer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.musicplayer.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    User findByEmail(String email);

    // Auth: tìm user không phân biệt trạng thái
    @Query("SELECT u FROM User u WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail")
    Optional<User> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

    // Auth: chỉ tìm user đang active
    Optional<User> findByUsernameOrEmailAndActiveTrue(String username, String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findAllByActiveTrue();

    Optional<User> findByIdAndActiveTrue(Long id);

    boolean existsByIdAndActiveTrue(Long id);
}
