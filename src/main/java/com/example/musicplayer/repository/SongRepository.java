package com.example.musicplayer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.musicplayer.entity.Song;

public interface SongRepository extends JpaRepository<Song, Long> {
    // Soft delete queries — chỉ trả về song đang active
    List<Song> findAllByIsActiveTrue();

    List<Song> findByArtistIdAndIsActiveTrue(Long artistId);

    List<Song> findByAlbumIdAndIsActiveTrue(Long albumId);

    List<Song> findByTitleAndIsActiveTrue(String title);

    // Cascade deactivation: tắt tất cả song của artist/album
    @Modifying
    @Query("UPDATE Song s SET s.isActive = false WHERE s.artist.id = :artistId AND s.isActive = true")
    void deactivateByArtistId(@Param("artistId") Long artistId);

    @Modifying
    @Query("UPDATE Song s SET s.isActive = false WHERE s.album.id = :albumId AND s.isActive = true")
    void deactivateByAlbumId(@Param("albumId") Long albumId);
}
