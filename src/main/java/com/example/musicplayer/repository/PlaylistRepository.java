package com.example.musicplayer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.musicplayer.entity.Playlist;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUserIdAndActiveTrue(Long userId);

    @Modifying
    @Query("UPDATE Playlist p SET p.active = false WHERE p.user.id = :userId AND p.active = true")
    void deactivateByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Playlist p SET p.active = true WHERE p.user.id = :userId AND p.active = false")
    void reactivateByUserId(@Param("userId") Long userId);
}
