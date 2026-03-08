package com.example.musicplayer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.musicplayer.entity.PlaylistSong;

public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, Long> {
    List<PlaylistSong> findByPlaylistId(Long playlistId);
    void deleteByPlaylistIdAndSongId(Long playlistId, Long songId);
}
