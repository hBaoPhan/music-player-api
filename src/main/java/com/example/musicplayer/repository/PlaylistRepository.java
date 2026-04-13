package com.example.musicplayer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.musicplayer.entity.Playlist;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    // Chỉ trả về playlist đang active
    List<Playlist> findByUserIdAndIsActiveTrue(Long userId);

    // Cascade soft delete: tắt tất cả playlist của user
    @Modifying
    @Query("UPDATE Playlist p SET p.isActive = false WHERE p.userId = :userId AND p.isActive = true")
    void deactivateByUserId(@Param("userId") Long userId);

    // Giữ lại để tương thích với deletePlaylistsByUserId (không dùng nữa nhưng để an toàn)
    void deleteByUserId(Long userId);
}
