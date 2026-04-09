package com.example.musicplayer.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.musicplayer.entity.PlaylistSong;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, Long> {
    List<PlaylistSong> findByPlaylistId(Long playlistId);

    void deleteByPlaylistIdAndSongId(Long playlistId, Long songId);

    boolean existsByPlaylistIdAndSongId(Long playlistId, Long songId);

    @Modifying
    @Query("DELETE FROM PlaylistSong ps WHERE ps.playlistId IN (SELECT p.id FROM Playlist p WHERE p.userId = :userId)")
    void deleteByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM PlaylistSong ps WHERE ps.playlistId = :playlistId")
    void deleteByPlaylistId(Long playlistId);
}
