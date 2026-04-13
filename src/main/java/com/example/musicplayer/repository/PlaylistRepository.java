package com.example.musicplayer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.musicplayer.entity.Playlist;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUserIdAndIsActiveTrue(Long userId);

    @Modifying
    @Query("UPDATE Playlist p SET p.isActive = false WHERE p.userId = :userId AND p.isActive = true")
    void deactivateByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Playlist p SET p.isActive = true WHERE p.userId = :userId AND p.isActive = false")
    void reactivateByUserId(@Param("userId") Long userId);
}
